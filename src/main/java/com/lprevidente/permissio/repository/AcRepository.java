package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AcRepository<T extends BaseEntity<ID>, ID>
    extends JpaRepositoryImplementation<T, ID> {

  EntityManager getEntityManager();

  /* Find By */

  Optional<T> findOne(AcCriteria acCriteria, Specification<T> specification);

  Optional<T> findOne(AcCriteria acCriteria, Specification<T> specification, String entityGraph);

  /* Find By ID */

  Optional<T> findById(ID id, String entityGraph);

  Optional<T> findById(ID id, AcCriteria acCriteria);

  Optional<T> findById(ID id, AcCriteria acCriteria, String entityGraph);

  /* Find All */

  List<T> findAll(AcCriteria acCriteria);

  List<T> findAll(AcCriteria acCriteria, String entityGraph);

  List<T> findAll(AcCriteria acCriteria, Sort sort);

  List<T> findAll(AcCriteria acCriteria, Sort sort, String entityGraph);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort, String entityGraph);

  List<T> findAll(Specification<T> specification, Sort sort, String entityGraph);

  /* Find All Page */

  Page<T> findAll(AcCriteria acCriteria, Pageable pageable);

  Page<T> findAll(AcCriteria acCriteria, Pageable pageable, String entityGraph);

  Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable);

  Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable, String entityGraph);

  /* Fina All By ID */

  List<T> findAllById(Collection<ID> ids, String entityGraph);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph);

  boolean existsById(ID id, AcCriteria acCriteria);

  Map<ID, Boolean> existsById(Collection<ID> ids, AcCriteria acCriteria);

  /* Related */

  Optional<T> findByIdRelated(ID id, AcCriteria acCriteria);

  Optional<T> findByIdRelated(ID id, AcCriteria acCriteria, String entityGraph);

  List<ID> findAllIdRelated(AcCriteria criteria);

  List<T> findAllRelated(AcCriteria acCriteria);

  List<T> findAllRelated(AcCriteria acCriteria, Sort sort);

  List<T> findAllRelated(AcCriteria acCriteria, Sort sort, String entityGraph);

  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria);

  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria, Sort sort);

  List<T> findAllRelatedById(Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph);
}
