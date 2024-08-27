package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface AcRepository<T extends BaseEntity> extends JpaRepositoryImplementation<T, Long> {

  EntityManager getEntityManager();

  Predicate getPredicate(Specification specification, Path<T> path, CriteriaBuilder cb);

  Predicate getPredicateRelated(Specification specification, Path<T> path, CriteriaBuilder cb);

  Optional<T> findById(long id, Specification specification);

  List<T> findAll(Specification specification);

  List<T> findAll(Specification specification, Sort sort);

  List<T> findAll(Specification specification, Sort sort, String entityGraph);

  List<T> findAllById(Collection<Long> ids, Specification specification);

  List<T> findAllById(Collection<Long> ids, Specification specification, Sort sort);

  List<T> findAllById(Collection<Long> ids, Specification specification, Sort sort, String entityGraph);

  List<T> findAllRelated(Specification specification);

  List<T> findAllRelated(Specification specification, Sort sort);

  List<T> findAllRelated(Specification specification, Sort sort, String entityGraph);

  boolean existsById(Long id, Specification specification);

  Map<Long, Boolean> existsById(Collection<Long> ids, Specification specification);
}
