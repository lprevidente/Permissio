package com.lprevidente.permissio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import com.lprevidente.permissio.restrictions.*;
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
}
