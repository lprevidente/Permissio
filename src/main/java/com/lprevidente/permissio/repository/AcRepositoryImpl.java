package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.restrictions.DisjunctionRestriction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class AcRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long>
    implements AcRepository<T> {
  protected static final String FETCH = "jakarta.persistence.fetchgraph";

  private final EntityManager em;
  private final Class<T> clazz;

  protected AcRepositoryImpl(EntityManager em, JpaEntityInformation entityInformation, Class<?> clazz) {
    super(entityInformation, em);
    this.em = em;
    this.clazz = (Class<T>) clazz;
  }

  private Predicate getPredicate(Specification specification, Path<T> path, CriteriaBuilder cb) {
    final var requester = specification.getRequester();
    final var permissions = specification.getPermissions();
    final var join = new HashMap<String, Join<?, ?>>();
    return permissions.stream()
        .map(p -> requester.getPermissions().getOrDefault(p, new DisjunctionRestriction()))
        .map(r -> r.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  @Override
  public Optional<T> findById(long id, Specification specification) {
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
    final var query = cq.distinct(true).where(predicate).orderBy();

    if (sort.isSorted()) query.orderBy(QueryUtils.toOrders(sort, root, cb));

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, entityGraph).getResultList();

    return typedQuery.getResultList();
  }

  @Override
  public List<T> findAllById(Iterable<Long> ids, Specification specification) {
    return findAllById(ids, specification, Sort.unsorted());
  }

  @Override
  public List<T> findAllById(Iterable<Long> ids, Specification specification, Sort sort) {
    return findAllById(ids, specification, sort, null);
  }

  @Override
  public List<T> findAllById(
      Iterable<Long> ids, Specification specification, Sort sort, String entityGraph) {
    final var cb = em.getCriteriaBuilder();
    final var cq = cb.createQuery(clazz);
    final var root = cq.from(clazz);

    final var predicate = getPredicate(specification, root, cb);
    final var query = cq.distinct(true).where(cb.and(predicate, root.get("id").in(ids))).orderBy();

    if (sort.isSorted()) query.orderBy(QueryUtils.toOrders(sort, root, cb));

    final var typedQuery = em.createQuery(query);
    if (entityGraph != null) return typedQuery.setHint(FETCH, entityGraph).getResultList();

    return typedQuery.getResultList();
  }
}
