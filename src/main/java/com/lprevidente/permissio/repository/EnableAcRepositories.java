package com.lprevidente.permissio.repository;

import java.lang.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Annotation to enable AcRepository support.
 *
 * <p>This enables the following repository interfaces:
 *
 * <ul>
 *   <li>{@link AcRepository} - Base interface with core access control methods
 *   <li>{@link AcRepositorySpecificationExecutor} - Support for specification-based queries
 *   <li>{@link AcRepositoryRelated} - Support for related entity access
 * </ul>
 *
 * <p>Usage example:
 *
 * <pre>
 * &#64;SpringBootApplication
 * &#64;EnableAcRepositories(basePackages = "com.example.repositories")
 * public class MyApplication {
 *   public static void main(String[] args) {
 *     SpringApplication.run(MyApplication.class, args);
 *   }
 * }
 * </pre>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableJpaRepositories(repositoryFactoryBeanClass = AcRepositoryFactoryBean.class)
public @interface EnableAcRepositories {
  /**
   * Alias for the {@link #basePackages()} attribute. Configures the base packages to scan for
   * repositories.
   *
   * @return the base packages to scan
   */
  String[] value() default {};

  /**
   * Base packages to scan for annotated components.
   *
   * @return the base packages to scan
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for
   * annotated components. The package of each class specified will be scanned.
   *
   * @return classes from the base packages to scan
   */
  Class<?>[] basePackageClasses() default {};
}
