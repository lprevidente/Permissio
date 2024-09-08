package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AcRepository<T extends BaseEntity<ID>, ID>
    extends JpaRepositoryImplementation<T, ID> {

  EntityManager getEntityManager();

  Predicate getPredicate(Specification specification, Path<T> path, CriteriaBuilder cb);

  Predicate getPredicateRelated(Specification specification, Path<T> path, CriteriaBuilder cb);

  Optional<T> findById(ID id, Specification specification);

  List<T> findAll(Specification specification);

  List<T> findAll(Specification specification, Sort sort);

  List<T> findAll(Specification specification, Sort sort, String entityGraph);

  Page<T> findAll(Specification specification, Pageable pageable);

  Page<T> findAll(Specification specification, Pageable pageable, String entityGraph);

  List<T> findAllById(Collection<ID> ids, Specification specification);

  List<T> findAllById(Collection<ID> ids, Specification specification, Sort sort);

  List<T> findAllById(
      Collection<ID> ids, Specification specification, Sort sort, String entityGraph);

  List<T> findAllRelated(Specification specification);

  List<T> findAllRelated(Specification specification, Sort sort);

  List<T> findAllRelated(Specification specification, Sort sort, String entityGraph);

  boolean existsById(ID id, Specification specification);

  Map<ID, Boolean> existsById(Collection<ID> ids, Specification specification);
}
