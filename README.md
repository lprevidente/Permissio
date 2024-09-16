# Java Library for Access Control and JPA Repository Management

This Java library provides functionality for managing access control based on permissions and restrictions on entities,
along with custom JPA repository implementations to handle advanced query lookup strategies.

## Features

- **Access Control Management**:
    - Provides functionality to restrict access to entities based on entity ID, creator, membership, handler and
      composed restriction.
    - Flexible support for combining multiple restrictions (`AND`, `OR` conditions).

- **Custom JPA Repository**:
    - Leverages Spring Data JPA with custom repository implementations.
    - Supports dynamic query building with Criteria API and customizable query lookup strategies.

## Requirements

- Java 17+
- Spring Boot 3+ or any Spring-based environment
- Maven or Gradle (for dependency management)
- Hibernate 6.x (or higher) for ORM functionality
- Jackson for JSON serialization/deserialization (optional)

## Getting Started

### Maven Dependency

To use this library, add the following dependency to your `pom.xml`:

```xml

<dependency>
  <groupId>com.lprevidente</groupId>
  <artifactId>permissio</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Spring Boot Configuration

**Enable JPA Repositories**:  
Ensure that your Spring Boot application has the necessary configuration to enable JPA repositories.

   ```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAcRepositories(basePackages = "com.example.repositories")
public class MyApplication {
  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }
}
 ```

### Usage

See the [example](https://github.com/lprevidente/Permissio/tree/main/src/test/java/com/lprevidente/permissio/repository)
provided in test folder to see how to use the library in depth.

#### Extending Restrictions

You can create your own custom restrictions by implementing the `Restriction` interface and registering the subtype with Jackson's `ObjectMapper`.

### Creating a Custom Restriction

1. **Implement the `Restriction` Interface**:

   ```java
   package com.lprevidente.permissio.restriction;

   import jakarta.persistence.criteria.CriteriaBuilder;
   import jakarta.persistence.criteria.Join;
   import jakarta.persistence.criteria.Path;
   import jakarta.persistence.criteria.Predicate;
   import java.util.Map;

   public class CustomRestriction implements Restriction<Object> {

     @Override
     public boolean isSatisfiedBy(Requester requester, Object baseEntity) {
       // Custom logic to determine if the restriction is satisfied
     }

     @Override
     public Predicate toPredicate(
         Requester requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
       // Custom logic to create a Predicate
     }
   }
   ```
2.Register the Subtype with ObjectMapper:  Ensure that your custom restriction is recognized by Jackson's ObjectMapper. This can be done in your Spring Boot configuration:

   ```java
    import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.lprevidente.permissio.restriction.CustomRestriction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerSubtypes(new NamedType(CustomRestriction.class, "customRestriction"));
    return objectMapper;
  }
}
```
By following these steps, you can extend the restriction functionality and ensure that your custom restrictions are properly serialized and deserialized by Jackson.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
