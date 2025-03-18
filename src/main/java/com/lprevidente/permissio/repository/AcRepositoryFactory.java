package com.lprevidente.permissio.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.NonNull;

class AcRepositoryFactory<T, ID> extends JpaRepositoryFactory {

  public AcRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
  }

  @NonNull
  @Override
  protected JpaRepositoryImplementation<T, ID> getTargetRepository(
      RepositoryInformation information, EntityManager entityManager) {
    JpaEntityInformation ei = getEntityInformation(information.getDomainType());

    if (information.getRepositoryBaseClass().equals(AcRepository.class))
      return new AcRepositoryImpl<>(entityManager, ei);

    return new SimpleJpaRepository(ei, entityManager);
  }

  @NonNull
  @Override
  protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
    if (AcRepository.class.isAssignableFrom(metadata.getRepositoryInterface()))
      return AcRepository.class;
    return SimpleJpaRepository.class;
  }
}
