package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.NonNull;

public class AcRepositoryFactory extends JpaRepositoryFactory {

  public AcRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
  }

  @NonNull
  @Override
  protected JpaRepositoryImplementation<BaseEntity, Long> getTargetRepository(
      RepositoryInformation information, EntityManager entityManager) {
    JpaEntityInformation<?, Serializable> ei = getEntityInformation(information.getDomainType());
    return new AcRepositoryImpl<>(entityManager, ei, information.getDomainType());
  }

  @NonNull
  @Override
  protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
    return AcRepositoryImpl.class;
  }
}
