package com.lprevidente.permissio.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OfficeRepository extends AcRepository<Office, Long> {

  @Query("select o from Office o where o.name = :name")
  Optional<Office> findByName(String name);

  List<Office> findAllByNameOrderByIdDesc(String name);

  default List<Office> findAllByName(String name) {
    return getEntityManager()
        .createQuery("select o from Office o where o.name = :name", Office.class)
        .setParameter("name", name)
        .getResultList();
  }
}
