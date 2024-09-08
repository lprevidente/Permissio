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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
