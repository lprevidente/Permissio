package com.lprevidente.permissio.repository;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.io.Serial;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional(readOnly = true)
public class AcRepositoryImpl<T extends BaseEntity<ID>, ID> extends SimpleJpaRepository<T, ID>
    implements AcRepository<T, ID> {

  protected static final String FETCH = "jakarta.persistence.fetchgraph";
  private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null";

  private final EntityManager em;
  private final Class<ID> idClazz;
  private final JpaEntityInformation<T, ?> entityInformation;

  protected AcRepositoryImpl(EntityManager em, JpaEntityInformation<T, ID> entityInformation) {
    super(entityInformation, em);
    this.entityInformation = entityInformation;
    this.em = em;
    this.idClazz = entityInformation.getIdType();
  }

  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Optional<T> findOne(AcCriteria acCriteria, Specification<T> spec) {
    return findOne(acCriteria, spec, null);
  }

  @Override
  public Optional<T> findOne(
      @Nullable AcCriteria acCriteria, @Nullable Specification<T> spec, String entityGraph) {
    return getQuery(spec, acCriteria, Sort.unsorted(), entityGraph).getResultStream().findFirst();
  }

  @Override
  public Optional<T> findById(ID id, String entityGraph) {
    Assert.notNull(id, ID_MUST_NOT_BE_NULL);
    return findOne(
        null,
        (root, query, cb) -> cb.equal(root.get(entityInformation.getIdAttribute()), id),
        entityGraph);
  }

  @Override
  public Optional<T> findById(ID id, AcCriteria acCriteria) {
    return findById(id, acCriteria, null);
  }

  @Override
  public Optional<T> findById(ID id, AcCriteria acCriteria, String entityGraph) {
    Assert.notNull(id, ID_MUST_NOT_BE_NULL);
    return findOne(
        acCriteria,
        (root, query, cb) -> cb.equal(root.get(entityInformation.getIdAttribute()), id),
        entityGraph);
  }

  @Override
  public List<T> findAll(AcCriteria acCriteria) {
    return findAll(acCriteria, Sort.unsorted());
  }

  @Override
  public List<T> findAll(AcCriteria acCriteria, String entityGraph) {
    return findAll(acCriteria, null, Sort.unsorted(), entityGraph);
  }

  @Override
  public List<T> findAll(AcCriteria acCriteria, Specification<T> specification) {
    return findAll(acCriteria, specification, Sort.unsorted(), null);
  }

  @Override
  public List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort) {
    return findAll(acCriteria, specification, sort, null);
  }

  @Override
  public List<T> findAll(
      AcCriteria acCriteria, Specification<T> specification, Sort sort, String entityGraph) {
    return getQuery(specification, acCriteria, sort, entityGraph).getResultList();
  }

  @Override
  public List<T> findAll(Specification<T> specification, Sort sort, String entityGraph) {
    return findAll(null, specification, sort, entityGraph);
  }

  public List<T> findAll(AcCriteria acCriteria, Sort sort) {
    return findAll(acCriteria, sort, null);
  }

  public List<T> findAll(AcCriteria acCriteria, Sort sort, @Nullable String entityGraph) {
    return getQuery(null, acCriteria, sort, entityGraph).getResultList();
  }

  @Override
  public Page<T> findAll(AcCriteria acCriteria, Pageable pageable) {
    return findAll(acCriteria, pageable, null);
  }

  @Override
  public Page<T> findAll(AcCriteria acCriteria, Pageable pageable, String entityGraph) {
    return findAll(acCriteria, null, pageable, entityGraph);
  }

  @Override
  public Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable) {
    return findAll(acCriteria, specification, pageable, null);
  }

  @Override
  public Page<T> findAll(
      @Nullable AcCriteria acCriteria,
      @Nullable Specification<T> specification,
      Pageable pageable,
      String entityGraph) {

    if (pageable.isUnpaged()) return new PageImpl<>(findAll(acCriteria));

    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(getDomainClass());

    applySpecificationToCriteria(specification, cq, acCriteria);

    final var typedQuery = applyEntityGraph(cq, entityGraph);

    return readPage(typedQuery, getDomainClass(), pageable, null);
  }

  @Override
  public List<T> findAllById(Collection<ID> ids, String entityGraph) {
    return findAllById(ids, null, Sort.unsorted(), entityGraph);
  }

  @Override
  public List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria) {
    return findAllById(ids, acCriteria, Sort.unsorted());
  }

  @Override
  public List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort) {
    return findAllById(ids, acCriteria, sort, null);
  }

  @Override
  public List<T> findAllById(
      Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph) {
    if (ids.isEmpty()) return Collections.emptyList();

    final var specification = new ByIdsSpecification<>(entityInformation);
    final var query = getQuery(specification, acCriteria, Sort.unsorted(), entityGraph);

    return query.setParameter(specification.parameter, ids).getResultList();
  }

  @Override
  public Optional<T> findByIdRelated(ID id, AcCriteria acCriteria) {
    final var tq =
        getQuery(
            (root, cq, cb) ->
                cb.and(
                    cb.equal(root.get(entityInformation.getIdAttribute()), id),
                    acCriteria.getPredicateRelated(root, cb)),
            Sort.unsorted());
    return tq.getResultStream().findFirst();
  }

  @Override
  public Optional<T> findByIdRelated(ID id, AcCriteria acCriteria, String entityGraph) {
    final var tq =
        getQuery(
            (root, cq, cb) ->
                cb.and(
                    cb.equal(root.get(entityInformation.getIdAttribute()), id),
                    acCriteria.getPredicateRelated(root, cb)),
            null,
            Sort.unsorted(),
            entityGraph);
    return tq.getResultStream().findFirst();
  }

  @Override
  public List<ID> findAllIdRelated(AcCriteria criteria) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(idClazz);
    final var root = cq.from(getDomainClass());

    cq.where(criteria.toPredicate(root, cb)).select(root.get("id")).distinct(true);

    return em.createQuery(cq).getResultList();
  }

  @Override
  public List<T> findAllRelated(AcCriteria acCriteria) {
    return findAllRelated(acCriteria, Sort.unsorted());
  }

  @Override
  public List<T> findAllRelated(AcCriteria acCriteria, Sort sort) {
    return findAllRelated(acCriteria, sort, null);
  }

  @Override
  public List<T> findAllRelated(AcCriteria acCriteria, Sort sort, String entityGraph) {
    final var tq = getQuery((root, cq, cb) -> acCriteria.getPredicateRelated(root, cb), sort);
    if (entityGraph != null) tq.setHint(FETCH, em.getEntityGraph(entityGraph));
    return tq.getResultList();
  }

  @Override
  public List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria) {
    return findAllRelatedById(ids, acCriteria, Sort.unsorted());
  }

  @Override
  public List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria, Sort sort) {
    return findAllRelatedById(ids, acCriteria, sort, null);
  }

  @Override
  public List<T> findAllRelatedById(
      Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph) {
    final var tq =
        getQuery(
            (root, cq, cb) ->
                cb.and(
                    root.get(entityInformation.getIdAttribute()).in(ids),
                    acCriteria.getPredicateRelated(root, cb)),
            sort);
    if (entityGraph != null) tq.setHint(FETCH, em.getEntityGraph(entityGraph));
    return tq.getResultList();
  }

  @Override
  public boolean existsById(ID id, AcCriteria acCriteria) {
    Assert.notNull(id, ID_MUST_NOT_BE_NULL);

    return exists(
        (root, cq, cb) ->
            cb.and(
                cb.equal(root.get(entityInformation.getIdAttribute()), id),
                acCriteria.toPredicate(root, cb)));
  }

  @Override
  public Map<ID, Boolean> existsById(Collection<ID> ids, AcCriteria acCriteria) {
    if (ids.isEmpty()) return Collections.emptyMap();

    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(idClazz);
    final var root = cq.from(getDomainClass());

    final var predicate =
        cb.and(
            root.get(entityInformation.getIdAttribute()).in(ids), acCriteria.toPredicate(root, cb));
    cq.where(predicate).select(root.get("id")).distinct(true);

    final var res = new HashMap<ID, Boolean>();
    em.createQuery(cq).getResultStream().forEach(id -> res.put(id, true));
    ids.forEach(id -> res.putIfAbsent(id, false));

    return res;
  }

  private TypedQuery<T> getQuery(
      @Nullable Specification<T> spec,
      @Nullable AcCriteria acCriteria,
      Sort sort,
      String entityGraph) {
    final var builder = em.getCriteriaBuilder();
    final var query = builder.createQuery(getDomainClass());

    final var root = applySpecificationToCriteria(spec, query, acCriteria);
    query.select(root);

    if (sort.isSorted()) {
      query.orderBy(toOrders(sort, root, builder));
    }

    return applyEntityGraph(query, entityGraph);
  }

  private TypedQuery<T> applyEntityGraph(CriteriaQuery<T> query, @Nullable String entityGraph) {
    Assert.notNull(query, "CriteriaQuery must not be null");

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, em.getEntityGraph(entityGraph));

    return typedQuery;
  }

  private Root<T> applySpecificationToCriteria(
      @Nullable Specification<T> spec, CriteriaQuery<T> query, @Nullable AcCriteria acCriteria) {
    Assert.notNull(query, "CriteriaQuery must not be null");

    final var root = query.from(getDomainClass());
    final var cb = em.getCriteriaBuilder();
    final var predicates = new ArrayList<Predicate>();

    if (acCriteria != null) predicates.add(acCriteria.toPredicate(root, cb));
    if (spec != null) predicates.add(spec.toPredicate(root, query, cb));

    query.distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

    return root;
  }

  private static final class ByIdsSpecification<T> implements Specification<T> {

    @Serial private static final long serialVersionUID = 1L;

    private final JpaEntityInformation<T, ?> entityInformation;

    @Nullable ParameterExpression<Collection<?>> parameter;

    ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
      this.entityInformation = entityInformation;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

      Path<?> path = root.get(entityInformation.getIdAttribute());
      parameter =
          (ParameterExpression<Collection<?>>) (ParameterExpression) cb.parameter(Collection.class);
      return path.in(parameter);
    }
  }
}
