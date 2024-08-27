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
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@EnableAcRepositories("com.lprevidente.permissio.repository")
@Sql(scripts = "classpath:users.sql")
@Sql(
    statements = "DELETE FROM users; DELETE FROM teams; DELETE FROM offices",
    executionPhase = AFTER_TEST_METHOD)
class RepositoryTest {

  @Autowired private UserRepository userRepository;
  @Autowired private OfficeRepository officeRepository;

  @Nested
  class FindByID {

    @Test
    void byId() {
      final var specification =
          Specification.builder()
              .request(new Requester(1, Map.of("user:read", new AccessByIdRestriction(1))))
              .permission("user:read")
              .build();

      final var user = userRepository.findById(1L, specification);
      assertThat(user).isPresent();
    }

    @Test
    void byCreator() {
      final var specification =
          Specification.builder()
              .request(
                  new Requester(
                      1, Map.of("user:read", new AccessByCreatorRestriction("creator:id"))))
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
              .request(new Requester(1, Map.of("office:read", new AccessByIdRestriction(1))))
              .permission("office:read")
              .build();

      final var offices = officeRepository.findAllById(List.of(1L, 2L), specification);
      assertThat(offices).isNotEmpty();
    }
  }

  @Nested
  class Exists {
    final Specification specification =
        Specification.builder()
            .request(
                Requester.builder()
                    .id(1)
                    .addPermission("office:read", new AccessByIdRestriction(1L))
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
