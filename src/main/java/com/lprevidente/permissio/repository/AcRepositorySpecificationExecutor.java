package com.lprevidente.permissio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface for repositories that support executing specifications.
 *
 * @param <T> The entity type
 */
@NoRepositoryBean
public interface AcRepositorySpecificationExecutor<T> {

  /**
   * Returns a single entity matching the given specification and access control criteria.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @return optional with matching entity or empty if none found
   */
  Optional<T> findOne(AcCriteria acCriteria, Specification<T> specification);

  /**
   * Returns a single entity matching the given specification, access control criteria, and entity
   * graph.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @param entityGraph the name of the entity graph to be used
   * @return optional with matching entity or empty if none found
   */
  Optional<T> findOne(AcCriteria acCriteria, Specification<T> specification, String entityGraph);

  /**
   * Returns all entities matching the given specification and access control criteria.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @return list of matching entities
   */
  List<T> findAll(AcCriteria acCriteria, Specification<T> specification);

  /**
   * Returns all entities matching the given specification, access control criteria, and sorting.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @param sort the sort specification to use
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort);

  /**
   * Returns all entities matching the given specification, access control criteria, sorting, and
   * entity graph.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAll(
      AcCriteria acCriteria, Specification<T> specification, Sort sort, String entityGraph);

  /**
   * Returns all entities matching the given specification, sorting, and entity graph.
   *
   * @param specification specification to match
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAll(Specification<T> specification, Sort sort, String entityGraph);

  /**
   * Returns a page of entities matching the given specification, access control criteria, and
   * pageable.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @param pageable pageable to request a paged result
   * @return page of matching entities
   */
  Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable);

  /**
   * Returns a page of entities matching the given specification, access control criteria, pageable,
   * and entity graph.
   *
   * @param acCriteria access control criteria
   * @param specification specification to match
   * @param pageable pageable to request a paged result
   * @param entityGraph the name of the entity graph to be used
   * @return page of matching entities
   */
  Page<T> findAll(
      AcCriteria acCriteria, Specification<T> specification, Pageable pageable, String entityGraph);
}
