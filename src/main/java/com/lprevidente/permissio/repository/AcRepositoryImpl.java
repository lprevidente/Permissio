package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.restrictions.AccessByRelatedEntityRestriction;
import com.lprevidente.permissio.restrictions.ConjunctionRestriction;
import com.lprevidente.permissio.restrictions.DisjunctionRestriction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class AcRepositoryImpl<T extends BaseEntity<ID>, ID> extends SimpleJpaRepository<T, ID>
    implements AcRepository<T, ID> {
  protected static final String FETCH = "jakarta.persistence.fetchgraph";

  private final EntityManager em;
  private final Class<T> clazz;
  private final Class<ID> idClazz;

  protected AcRepositoryImpl(EntityManager em, JpaEntityInformation<T, ID> entityInformation) {
    super(entityInformation, em);
    this.em = em;
    this.clazz = entityInformation.getJavaType();
    this.idClazz = entityInformation.getIdType();
  }

  public EntityManager getEntityManager() {
    return em;
  }

  public Predicate getPredicate(Specification specification, Path<T> path, CriteriaBuilder cb) {
    final var requester = specification.getRequester();
    final var permissions = specification.getPermissions();
    final var join = new HashMap<String, Join<?, ?>>();
    return permissions.stream()
        .map(p -> requester.getPermissions().getOrDefault(p, new DisjunctionRestriction()))
        .map(r -> r.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  public Predicate getPredicateRelated(
      Specification specification, Path<T> path, CriteriaBuilder cb) {
    final var requester = specification.getRequester();
    final var permissions = specification.getPermissions();
    final var join = new HashMap<String, Join<?, ?>>();

    return permissions.stream()
        .map(p -> requester.getPermissions().getOrDefault(p, new ConjunctionRestriction()))
        .map(
            r -> {
              if (r instanceof AccessByRelatedEntityRestriction relatedRestriction)
                return relatedRestriction.getRestriction().toPredicate(requester, path, cb, join);
              return cb.conjunction();
            })
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  @Override
  public Optional<T> findById(ID id, Specification specification) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate =
        cb.and(cb.equal(root.get("id"), id), getPredicate(specification, root, cb));
    return em.createQuery(cq.where(predicate)).getResultStream().findFirst();
  }

  @Override
  public List<T> findAll(Specification specification) {
    return findAll(specification, Sort.unsorted());
  }

  public List<T> findAll(Specification specification, Sort sort) {
    return findAll(specification, sort, null);
  }

  public List<T> findAll(Specification specification, Sort sort, @Nullable String entityGraph) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate = getPredicate(specification, root, cb);
    final var query = cq.distinct(true).where(predicate);

    if (sort.isSorted()) query.orderBy(QueryUtils.toOrders(sort, root, cb));

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, entityGraph).getResultList();

    return typedQuery.getResultList();
  }

  @Override
  public Page<T> findAll(Specification specification, Pageable pageable) {
    return findAll(specification, pageable, null);
  }

  @Override
  public Page<T> findAll(Specification specification, Pageable pageable, String entityGraph) {
    if (pageable.isUnpaged()) return new PageImpl<>(findAll(specification));

    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate = getPredicate(specification, root, cb);
    final var query = cq.distinct(true).where(predicate);

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) typedQuery.setHint(FETCH, entityGraph);

    return readPage(typedQuery, getDomainClass(), pageable, null);
  }

  @Override
  public List<T> findAllById(Collection<ID> ids, Specification specification) {
    return findAllById(ids, specification, Sort.unsorted());
  }

  @Override
  public List<T> findAllById(Collection<ID> ids, Specification specification, Sort sort) {
    return findAllById(ids, specification, sort, null);
  }

  @Override
  public List<T> findAllById(
      Collection<ID> ids, Specification specification, Sort sort, String entityGraph) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate = getPredicate(specification, root, cb);
    final var query = cq.distinct(true).where(cb.and(predicate, root.get("id").in(ids)));

    if (sort.isSorted()) query.orderBy(QueryUtils.toOrders(sort, root, cb));

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, entityGraph).getResultList();

    return typedQuery.getResultList();
  }

  @Override
  public List<T> findAllRelated(Specification specification) {
    return findAllRelated(specification, Sort.unsorted());
  }

  @Override
  public List<T> findAllRelated(Specification specification, Sort sort) {
    return findAllRelated(specification, sort, null);
  }

  @Override
  public List<T> findAllRelated(Specification specification, Sort sort, String entityGraph) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate = getPredicateRelated(specification, root, cb);
    final var query = cq.distinct(true).where(cb.and(predicate)).orderBy();

    if (sort.isSorted()) query.orderBy(QueryUtils.toOrders(sort, root, cb));

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, entityGraph).getResultList();

    return typedQuery.getResultList();
  }

  @Override
  public boolean existsById(ID id, Specification specification) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(Boolean.class);
    final var root = cq.from(clazz);

    final var predicate =
        cb.and(cb.equal(root.get("id"), id), getPredicate(specification, root, cb));
    cq.where(predicate).select(cb.literal(true));
    return em.createQuery(cq).getResultStream().findFirst().orElse(false);
  }

  @Override
  public Map<ID, Boolean> existsById(Collection<ID> ids, Specification specification) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(idClazz);
    final var root = cq.from(clazz);

    final var predicate = cb.and(root.get("id").in(ids), getPredicate(specification, root, cb));
    cq.where(predicate).select(root.get("id")).distinct(true);

    final var res = new HashMap<ID, Boolean>();
    em.createQuery(cq).getResultStream().forEach(id -> res.put(id, true));
    ids.forEach(id -> res.putIfAbsent(id, false));

    return res;
  }
}
