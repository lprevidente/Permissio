package com.lprevidente.permissio.repository;

import java.lang.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableJpaRepositories(repositoryFactoryBeanClass = AcRepositoryFactoryBean.class)
public @interface EnableAcRepositories {
  String[] value() default {};

  String[] basePackages() default {};

  Class<?>[] basePackageClasses() default {};
}
