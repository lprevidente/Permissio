package com.lprevidente.permissio.repository;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface providing core methods for entity access control. This interface
 * combines all specialized access control repository interfaces.
 *
 * @param <T> The entity type
 * @param <ID> The type of the entity's identifier
 */
@NoRepositoryBean
public interface AcRepository<T, ID> extends JpaRepositoryImplementation<T, ID> {

  /**
   * Returns the EntityManager used by this repository.
   *
   * @return the EntityManager
   */
  EntityManager getEntityManager();

  /**
   * Retrieves an entity by its id using a specified entity graph.
   *
   * @param id must not be {@literal null}
   * @param entityGraph the name of the entity graph to be used
   * @return the entity with the given id or {@literal Optional#empty()} if none found
   */
  Optional<T> findById(ID id, String entityGraph);

  /**
   * Retrieves an entity by its id with access control criteria.
   *
   * @param id must not be {@literal null}
   * @param acCriteria access control criteria
   * @return the entity with the given id or {@literal Optional#empty()} if none found
   */
  Optional<T> findById(ID id, AcCriteria acCriteria);

  /**
   * Retrieves an entity by its id with access control criteria and entity graph.
   *
   * @param id must not be {@literal null}
   * @param acCriteria access control criteria
   * @param entityGraph the name of the entity graph to be used
   * @return the entity with the given id or {@literal Optional#empty()} if none found
   */
  Optional<T> findById(ID id, AcCriteria acCriteria, String entityGraph);

  /**
   * Returns all entities with access control criteria.
   *
   * @param acCriteria access control criteria
   * @return all entities matching the criteria
   */
  List<T> findAll(AcCriteria acCriteria);

  /**
   * Returns all entities with access control criteria and entity graph.
   *
   * @param acCriteria access control criteria
   * @param entityGraph the name of the entity graph to be used
   * @return all entities matching the criteria
   */
  List<T> findAll(AcCriteria acCriteria, String entityGraph);

  /**
   * Returns all entities with access control criteria and sorting.
   *
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @return all entities matching the criteria sorted according to the specification
   */
  List<T> findAll(AcCriteria acCriteria, Sort sort);

  /**
   * Returns all entities with access control criteria, sorting, and entity graph.
   *
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return all entities matching the criteria sorted according to the specification
   */
  List<T> findAll(AcCriteria acCriteria, Sort sort, String entityGraph);

  /**
   * Returns all entities with the given IDs and entity graph.
   *
   * @param ids collection of IDs
   * @param entityGraph the name of the entity graph to be used
   * @return list of entities with the given IDs
   */
  List<T> findAllById(Collection<ID> ids, String entityGraph);

  /**
   * Returns all entities with the given IDs and access control criteria.
   *
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @return list of entities with the given IDs matching the criteria
   */
  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria);

  /**
   * Returns all entities with the given IDs, access control criteria, and sorting.
   *
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @return list of entities with the given IDs matching the criteria
   */
  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort);

  /**
   * Returns all entities with the given IDs, access control criteria, sorting, and entity graph.
   *
   * @param ids collection of IDs
   * @param acCriteria access control criteria
   * @param sort the sort specification to use
   * @param entityGraph the name of the entity graph to be used
   * @return list of entities with the given IDs matching the criteria
   */
  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph);

  /**
   * Returns whether an entity with the given id exists and meets the access control criteria.
   *
   * @param id must not be {@literal null}
   * @param acCriteria access control criteria
   * @return true if an entity exists with the given id and meets the criteria
   */
  boolean existsById(ID id, AcCriteria acCriteria);

  /**
   * Returns a map indicating whether entities with the given ids exist and meet the access control
   * criteria.
   *
   * @param ids collection of IDs to check
   * @param acCriteria access control criteria
   * @return map of ids to existence indicator
   */
  Map<ID, Boolean> existsById(Collection<ID> ids, AcCriteria acCriteria);

  /**
   * Returns a page of entities based on access control criteria and pageable.
   *
   * @param acCriteria access control criteria
   * @param pageable pageable to request a paged result
   * @return page of matching entities
   */
  Page<T> findAll(AcCriteria acCriteria, Pageable pageable);

  /**
   * Returns a page of entities based on access control criteria, pageable, and entity graph.
   *
   * @param acCriteria access control criteria
   * @param pageable pageable to request a paged result
   * @param entityGraph the name of the entity graph to be used
   * @return page of matching entities
   */
  Page<T> findAll(AcCriteria acCriteria, Pageable pageable, String entityGraph);
}
