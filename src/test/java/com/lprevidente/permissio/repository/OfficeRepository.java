package com.lprevidente.permissio.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OfficeRepository extends AcRepository<Office> {

  @Query("select o from Office o where o.name = :name")
  Optional<Office> findByName(String name);

  List<Office> findAllByNameOrderByIdDesc(String name);
}
