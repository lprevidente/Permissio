package com.lprevidente.permissio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.lprevidente.permissio.entity.Requester;
import com.lprevidente.permissio.restriction.*;
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
@Sql(scripts = {"classpath:users.sql", "classpath:products.sql"})
class RepositoryTest {

  @Autowired private UserRepository userRepository;
  @Autowired private OfficeRepository officeRepository;
  @Autowired private ProductRepository productRepository;

  @Nested
  class FindByID {

    @Test
    void byIdLong() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("user:read", new ById("id", 1))))
              .permission("user:read")
              .build();

      final var user = userRepository.findById(1L, criteria);
      assertThat(user).isPresent();
    }

    @Test
    void byIdString() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("product:read", new ById("id", "SKU001"))))
              .permission("product:read")
              .build();
      final var product = productRepository.findById("SKU001", criteria);
      assertThat(product).isPresent();
    }

    @Test
    void byCreator() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("user:read", new ByCreator("creator.id"))))
              .permission("user:read")
              .build();

      final var user = userRepository.findById(2L, criteria);
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
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("office:read", new ById("id", 1))))
              .permission("office:read")
              .build();

      final var offices = officeRepository.findAllById(List.of(1L, 2L), criteria);
      assertThat(offices).isNotEmpty();
    }
  }

  @Nested
  class FindAllPagable {

    @Test
    void findAllUnpaged() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("office:read", new ById("id", 1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(criteria, Pageable.unpaged());
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void findAllPagedOne() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("office:read", new ById("id", 1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(criteria, Pageable.ofSize(5).withPage(0));
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void findAllPagedOver() {
      final var criteria =
          AcCriteria.builder()
              .request(new Requester<>(1L, Map.of("office:read", new ById("id", 1))))
              .permission("office:read")
              .build();

      final var page = officeRepository.findAll(criteria, Pageable.ofSize(5).withPage(10));
      assertThat(page.getTotalElements()).isEqualTo(1);
      assertThat(page.getTotalPages()).isEqualTo(1);
      assertThat(page.getContent()).isEmpty();
    }
  }

  @Nested
  class Exists {
    private final AcCriteria acCriteria =
        AcCriteria.builder()
            .request(new Requester<>(1L, Map.of("office:read", new ById("id", 1))))
            .permission("office:read")
            .build();

    @Test
    void existsById() {
      final var exists = officeRepository.existsById(1L, acCriteria);
      assertThat(exists).isTrue();
    }

    @Test
    void existsByIds() {
      final var exists = officeRepository.existsById(List.of(1L, 2L), acCriteria);
      assertThat(exists).containsKeys(1L, 2L);
      assertThat(exists).extractingByKey(1L).isEqualTo(true);
      assertThat(exists).extractingByKey(2L).isEqualTo(false);
    }
  }
}
