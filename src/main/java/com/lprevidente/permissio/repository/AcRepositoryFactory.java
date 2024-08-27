package com.lprevidente.permissio.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

public class AcRepositoryFactory extends RepositoryFactorySupport {
  private final EntityManager entityManager;

  public AcRepositoryFactory(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @NonNull
  @Override
  public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
    return (EntityInformation<T, ID>) JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
  }

  @NonNull
  @Override
  protected Object getTargetRepository(RepositoryInformation information) {
    return new AcRepositoryImpl<>(entityManager, information.getDomainType());
  }

  @NonNull
  @Override
  protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
    return AcRepositoryImpl.class;
  }
}
