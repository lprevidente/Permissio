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

  Optional<T> findBy(AcCriteria acCriteria);

  Optional<T> findBy(AcCriteria acCriteria, Specification<T> specification);

  Optional<T> findBy(AcCriteria acCriteria, Specification<T> specification, String entityGraph);

  Optional<T> findById(ID id, AcCriteria acCriteria);

  Optional<T> findById(ID id, AcCriteria acCriteria, String entityGraph);

  List<T> findAll(AcCriteria acCriteria);

  List<T> findAll(AcCriteria acCriteria, String entityGraph);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort);

  List<T> findAll(AcCriteria acCriteria, Specification<T> specification, Sort sort, String entityGraph);

  List<T> findAll(AcCriteria acCriteria, Sort sort);

  List<T> findAll(AcCriteria acCriteria, Sort sort, String entityGraph);

  Page<T> findAll(AcCriteria acCriteria, Pageable pageable);

  Page<T> findAll(AcCriteria acCriteria, Pageable pageable, String entityGraph);

  Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable);

  Page<T> findAll(AcCriteria acCriteria, Specification<T> specification, Pageable pageable, String entityGraph);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort);

  List<T> findAllById(Collection<ID> ids, AcCriteria acCriteria, Sort sort, String entityGraph);

  List<T> findAllRelated(AcCriteria acCriteria);

  List<T> findAllRelated(AcCriteria acCriteria, Sort sort);

  List<T> findAllRelated(AcCriteria acCriteria, Sort sort, String entityGraph);

  boolean existsById(ID id, AcCriteria acCriteria);

  Map<ID, Boolean> existsById(Collection<ID> ids, AcCriteria acCriteria);
}
