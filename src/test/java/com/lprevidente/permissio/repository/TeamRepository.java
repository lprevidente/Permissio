package com.lprevidente.permissio.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeamRepository extends AcRepository<Team, Long> {}
