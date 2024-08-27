package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class AcRepositoryFactoryBean<T extends AcRepository<S>, S extends BaseEntity>
    extends TransactionalRepositoryFactoryBeanSupport<T, S, Long> {

  @Nullable private EntityManager entityManager;

  public AcRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void setMappingContext(@NonNull MappingContext<?, ?> mappingContext) {
    super.setMappingContext(mappingContext);
  }

  @Override
  public void setQueryLookupStrategyKey(QueryLookupStrategy.Key queryLookupStrategyKey) {
    super.setQueryLookupStrategyKey(queryLookupStrategyKey);
  }

  public void setEscapeCharacter(char escapeCharacter) {}

  @Override
  @NonNull
  protected RepositoryFactorySupport doCreateRepositoryFactory() {
    Assert.state(entityManager != null, "EntityManager must not be null");
    return new AcRepositoryFactory(entityManager);
  }

  @Override
  public void afterPropertiesSet() {
    Assert.state(entityManager != null, "EntityManager must not be null");
    super.afterPropertiesSet();
  }
}
