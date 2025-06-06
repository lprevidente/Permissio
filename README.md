# Java Library for Access Control and JPA Repository Management

[![Run Tests](https://github.com/lprevidente/Permissio/actions/workflows/run-tests.yml/badge.svg)](https://github.com/lprevidente/Permissio/actions/workflows/run-tests.yml)

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
    - Modular repository interfaces for different query types.

- **JSON Serialization/Deserialization**:
    - Built-in support for serializing and deserializing restrictions using Jackson.
    - Easily store and retrieve access control rules in JSON format.

## Requirements

- Java 21+
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
  <version>0.0.1</version>
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

## Repository Interfaces

The library provides a modular approach with specialized repository interfaces that you can use based on your needs:

### AcRepository

The main repository interface that extends all other interfaces, providing comprehensive access control functionality.
Use this when you need all features.

```java
public interface UserRepository extends AcRepository<User, Long> {
  // Custom methods
}
```

### AcRepositorySpecificationExecutor

Specialized interface for repositories that need to execute JPA Specifications with access control criteria.

```java
public interface ProductRepository
    extends JpaRepository<Product, Long>, AcRepositorySpecificationExecutor<Product, Long> {

  // Using specifications
  default List<Product> findActiveProductsByCategory(String category, Requester requester) {
    AcCriteria criteria = AcCriteria.builder().request(requester).build();

    Specification<Product> spec =
        (root, query, cb) ->
            cb.and(cb.equal(root.get("category"), category), cb.equal(root.get("active"), true));

    return findAll(criteria, spec);
  }
}

```

### AcRepositoryRelated

Specialized interface for repositories that need to work with related entities and entity graphs.

```java
public interface TeamRepository
    extends JpaRepository<Team, Long>, AcRepositoryRelated<Team, Long> {}

```

## Available Restrictions

The library provides various restriction types to control access to entities:

| Restriction       | Description                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| `ById`            | Restricts access to an entity based on a specific ID property.              |
| `ByCreator`       | Restricts access to entities created by the current requester.              |
| `ByMember`        | Restricts access to entities where the requester is a member.               |
| `ByHandler`       | Restricts access based on handlers with specific type and ID.               |
| `ByRelatedEntity` | Restricts access based on a related entity's properties.                    |
| `And`             | Combines multiple restrictions with AND logic.                              |
| `Or`              | Combines multiple restrictions with OR logic.                               |
| `Conjunction`     | Represents a specialized conjunction (all conditions must be true).         |
| `Disjunction`     | Represents a specialized disjunction (at least one condition must be true). |

### Restriction Details

#### ById

Restricts access to entities with a specific property value.

```java
new ById("id",1); // Access entity with id = 1
```

#### ByCreator

Restricts access to entities created by the current requester.

```java
new ByCreator("creator.id"); // Access entities where creator.id equals requester's ID
new ByCreator("creatorId");  // Access entities where creatorId equals requester's ID
```

#### ByMember

Restricts access to entities where the requester is a member.

```java
new ByMember("members.id");      // For one-to-many relationships
new ByMember("members.user.id"); // For custom join relationships
```

#### ByHandler

Restricts access based on handlers with specific type and ID.

```java
// Any handler type where handler.id matches the requester's ID
new ByHandler(
    new Type("*", "handlers.type"), 
    new Id("id","handlers.handler.id")
);

// Only HR type handlers where handler.id matches the requester's ID
new ByHandler(
    new Type("HR", "handlers.type"),
    new Id("id","handlers.id")
);
```

#### ByRelatedEntity

Restricts access based on properties of a related entity.

```java
// Access users related to office with ID 1
new ByRelatedEntity("office",new ById("id", 1L));

// Access users who are members of teams with ID 1
new ByRelatedEntity("teams.team",new ById("id", 1L));
```

#### Composite Restrictions (And/Or)

Combine multiple restrictions with logical operators.

```java
// Access entities that match both restrictions
new And(new ById("id", 2L), new ByCreator("creator.id"));

// Access entities that match either restriction
new Or(new ById("id", 1L), new ByCreator("creator.id"));
```

## Serialization

All restrictions can be serialized to and deserialized from JSON, making it easy to store and load access control rules.
Jackson is used for JSON processing with type information included in the `@type` property.

### JSON Representation of Restrictions

#### ById

```json
{
  "@type": "byId",
  "property": "id",
  "id": 1
}
```

#### ByCreator

```json
{
  "@type": "byCreator",
  "property": "creator.id"
}
```

#### ByMember

```json
{
  "@type": "byMember",
  "property": "members.id"
}
```

#### ByHandler

```json
{
  "@type": "byHandler",
  "type": {
    "type": "*",
    "property": "handlers.handler"
  },
  "id": {
    "field": "id",
    "property": "handlers.handler"
  }
}
```

#### Composite Restrictions (And/Or)

```json
{
  "@type": "and",
  "restrictions": [
    {
      "@type": "byId",
      "property": "id",
      "id": 1
    },
    {
      "@type": "byCreator",
      "property": "creator.id"
    }
  ]
}
```

#### ByRelatedEntity

```json
{
  "@type": "byRelatedEntity",
  "property": "team",
  "restriction": {
    "@type": "byId",
    "property": "id",
    "id": 1
  }
}
```

### Serialization Example

```java
// Create a restriction
Restriction restriction = new ByRelatedEntity("team", new ById("id", 1));

// Serialize to JSON
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(restriction);

// Deserialize from JSON
Restriction deserializedRestriction = mapper.readValue(json, Restriction.class);
```

## Usage Examples

### Basic Usage

Define a requester with permissions and restrictions:

```java
// Create a requester with ID 1 who can read users they created
Requester<Long> requester = Requester.builder()
        .id(1L)
        .addPermission("user:read", new ByCreator("creator.id"))
        .build();

// Build criteria with the requester and permission
AcCriteria criteria = AcCriteria.builder()
    .request(requester)
    .permission("user:read")
    .build();

// Use the criteria with your repository
List<User> users = userRepository.findAll(criteria);
```

### Combining Restrictions

```java
// Create a restriction that allows access to entity with ID 2 only if the requester is also the creator
Restriction restriction = new And(
        new ById("id", 2L),
        new ByCreator("creator.id")
    );

// Apply the restriction to a specific permission
Requester<Long> requester = new Requester<>(1L, Map.of("user:read", restriction));

// Use in a criteria query
AcCriteria criteria = AcCriteria.builder()
    .request(requester)
    .permission("user:read")
    .build();

// Execute the query
List<User> users = userRepository.findAll(criteria);
```

### Accessing Related Entities

```java
// Access users who belong to teams with ID 1 or 2
Restriction restriction = new ByRelatedEntity(
        "teams.team",
        new Or(
            new ById("id", 1L),
            new ById("id", 2L)
        )
    );

// Apply restriction to the "user:read" permission
Requester<Long> requester = new Requester<>(1L, Map.of("user:read", restriction));

// Build and execute the query
AcCriteria criteria = AcCriteria.builder()
    .request(requester)
    .permission("user:read")
    .build();

List<User> users = userRepository.findAll(criteria);
```

See
the [examples](https://github.com/lprevidente/Permissio/tree/main/src/test/java/com/lprevidente/permissio/repository) in
the test package for more detailed usage scenarios.

### Extending Restrictions

You can create your own custom restrictions by implementing the `Restriction` interface and registering the subtype with
Jackson's `ObjectMapper`.

#### Creating a Custom Restriction

1. **Implement the `Restriction` Interface**:

   ```java
   package com.lprevidente.permissio.restriction;

   import com.lprevidente.permissio.entity.Requester;
   import jakarta.persistence.criteria.CriteriaBuilder;
   import jakarta.persistence.criteria.Join;
   import jakarta.persistence.criteria.Path;
   import jakarta.persistence.criteria.Predicate;
   import java.util.Map;
    
    public record CustomRestriction() implements Restriction<Requester> {
      
        @Override
        public Predicate toPredicate(
            Requester requester,
            Path<?> path,
            CriteriaBuilder cb,
            Map<String, Join> joinMap) {
              // Your custom logic here
              return cb.equal(path.get("yourProperty"), requester.getId());
        }
    }
   ```

2. **Register the Subtype with ObjectMapper**:  
   Ensure that your custom restriction is recognized by Jackson's ObjectMapper:

   ```java
   import com.fasterxml.jackson.databind.ObjectMapper;
   import com.fasterxml.jackson.databind.jsontype.NamedType;
   import com.lprevidente.permissio.restriction.CustomRestriction;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;

   @Configuration
   class JacksonConfig {

     @Bean
      ObjectMapper objectMapper() {
       ObjectMapper objectMapper = new ObjectMapper();
       objectMapper.registerSubtypes(new NamedType(CustomRestriction.class, "customRestriction"));
       return objectMapper;
     }
   }
   ```

By following these steps, you can extend the restriction functionality and ensure that your custom restrictions are
properly serialized and deserialized by Jackson.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
