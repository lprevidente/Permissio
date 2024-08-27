package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface AcRepository<T extends BaseEntity> extends Repository<T, Long> {

  Optional<T> findById(long id, Specification specification);

  List<T> findAll(Specification specification);

  List<T> findAll(Specification specification, Sort sort);

  List<T> findAll(Specification specification, Sort sort, String entityGraph);

  T save(T entity);

  void delete(T entity);
}
