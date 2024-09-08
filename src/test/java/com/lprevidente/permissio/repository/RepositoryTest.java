package com.lprevidente.permissio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import com.lprevidente.permissio.restrictions.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@EnableAcRepositories(basePackageClasses = {UserRepository.class, OfficeRepository.class})
@Sql(
    scripts = {
      "classpath:users.sql",
      "classpath:products.sql",
    })
@Sql(
    statements = "DELETE FROM users; DELETE FROM teams; DELETE FROM offices; DELETE FROM products;",
    executionPhase = AFTER_TEST_METHOD)
class RepositoryTest {

  @Autowired private UserRepository userRepository;
  @Autowired private OfficeRepository officeRepository;
  @Autowired private ProductRepository productRepository;

  @Nested
  class FindByID {

    @Test
    void byIdLong() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", new AccessByIdRestriction<>(1))))
              .permission("user:read")
              .build();

      final var user = userRepository.findById(1L, specification);
      assertThat(user).isPresent();
    }

    @Test
    void byIdString() {
      final var specification =
          Specification.builder()
              .request(
                  new Requester(1L, Map.of("product:read", new AccessByIdRestriction<>("SKU001"))))
              .permission("product:read")
              .build();
      final var product = productRepository.findById("SKU001", specification);
      assertThat(product).isPresent();
    }

    @Test
    void byCreator() {
      final var specification =
          Specification.builder()
              .request(
                  new Requester(
                      1L, Map.of("user:read", new AccessByCreatorRestriction("creator:id"))))
              .permission("user:read")
              .build();

      final var user = userRepository.findById(2L, specification);
      assertThat(user).isPresent();
    }
  }

  @Nested
  class CustomQuery {

    @Test
    void findByName() {
      final var office = officeRepository.findByName("Head Office");
      assertThat(office).isPresent();
    }

    @Test
    void findAllByNameOrOrderByIdDesc() {
      final var offices = officeRepository.findAllByNameOrderByIdDesc("Head Office");
      assertThat(offices).isNotEmpty();
    }

    @Test
    void findAllByName() {
      final var offices = officeRepository.findAllByName("Head Office");
      assertThat(offices).isNotEmpty();
    }
  }

  @Nested
  class FindAllById {

    @Test
    void findAllById() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", new AccessByIdRestriction<>(1))))
              .permission("office:read")
              .build();

      final var offices = officeRepository.findAllById(List.of(1L, 2L), specification);
      assertThat(offices).isNotEmpty();
    }
  }

  @Nested
  class FindAllPagable {

    @Test
    void findAllUnpaged() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", new AccessByIdRestriction<>(1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(specification, Pageable.unpaged());
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void findAllPagedOne() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", new AccessByIdRestriction<>(1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(specification, Pageable.ofSize(5).withPage(0));
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void findAllPagedOver() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", new AccessByIdRestriction<>(1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(specification, Pageable.ofSize(5).withPage(10));
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).isEmpty();
    }
  }

  @Nested
  class Exists {
    final Specification specification =
        Specification.builder()
            .request(
                Requester.builder()
                    .id(1)
                    .addPermission("office:read", new AccessByIdRestriction<>(1L))
                    .build())
            .permission("office:read")
            .build();

    @Test
    void existsById() {
      final var exists = officeRepository.existsById(1L, specification);
      assertThat(exists).isTrue();
    }

    @Test
    void existsByIds() {
      final var exists = officeRepository.existsById(List.of(1L, 2L), specification);
      assertThat(exists).containsKeys(1L, 2L);
      assertThat(exists).extractingByKey(1L).isEqualTo(true);
      assertThat(exists).extractingByKey(2L).isEqualTo(false);
    }
  }
}
