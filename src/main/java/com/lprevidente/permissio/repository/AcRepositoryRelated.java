package com.lprevidente.permissio.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface for repositories that support related entity access.
 * 
 * @param <T> The entity type
 * @param <ID> The type of the entity's identifier
 */
@NoRepositoryBean
public interface AcRepositoryRelated<T, ID> {

  /**
   * Finds an entity by ID with related entity access based on access control criteria.
   * 
   * @param id must not be {@literal null}
   * @param acCriteria access control criteria
   * @return optional with matching entity or empty if none found
   */
  Optional<T> findByIdRelated(ID id, AcCriteria acCriteria);

  /**
   * Finds an entity by ID with related entity access based on access control criteria and entity graph.
   * 
   * @param id must not be {@literal null}
   * @param acCriteria access control criteria
   * @param entityGraph the name of the entity graph to be used
   * @return optional with matching entity or empty if none found
   */
  Optional<T> findByIdRelated(ID id, AcCriteria acCriteria, String entityGraph);

  /**
   * Finds all entity IDs with related entity access based on access control criteria.
   * 
   * @param criteria access control criteria
   * @return list of entity IDs
   */
  List<ID> findAllIdRelated(AcCriteria criteria);

  /**
   * Finds all entities with related entity access based on access control criteria.
   * 
   * @param acCriteria access control criteria
   * @return list of matching entities
   */
  List<T> findAllRelated(AcCriteria acCriteria);

  /**
   * Finds all entities with related entity access based on access control criteria and sorting.
   * 
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAllRelated(AcCriteria acCriteria, Sort sort);

  /**
   * Finds all entities with related entity access based on access control criteria, sorting, and entity graph.
   * 
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAllRelated(AcCriteria acCriteria, Sort sort, String entityGraph);

  /**
   * Finds all entities with the given IDs and related entity access based on access control criteria.
   * 
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @return list of matching entities
   */
  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria);

  /**
   * Finds all entities with the given IDs and related entity access based on access control criteria and sorting.
   * 
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria, Sort sort);

  /**
   * Finds all entities with the given IDs and related entity access based on access control criteria, sorting, and entity graph.
   * 
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return list of matching entities sorted according to the specification
   */
  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph);
}
