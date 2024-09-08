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
@EnableAcRepositories(basePackages = "com.lprevidente.permissio.repository")
@Sql(scripts = "classpath:users.sql")
@Sql(
    statements = "DELETE FROM users; DELETE FROM teams; DELETE FROM offices;",
    executionPhase = AFTER_TEST_METHOD)
class PredicateTest {

  @Autowired private UserRepository userRepository;
  @Autowired private TeamRepository teamRepository;
  @Autowired private OfficeRepository officeRepository;

  @Nested
  class VoidTest {

    @Test
    void noHasPermission() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of()))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).isEmpty();
    }

    @Test
    void noPermission() {
      final var specification =
          Specification.builder().request(new Requester(1L, Map.of())).build();

      final var users = userRepository.findAll(specification);
      assertThat(users).isNotEmpty();
    }
  }

  @Nested
  class AccessById {
    @Test
    void byId() {
      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", new AccessByIdRestriction<>(1))))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class AccessByCreator {

    @Test
    void byCreatorWithObj() {
      final var specification =
          Specification.builder()
              .request(
                  new Requester(1L, Map.of("user:read", new AccessByCreatorRestriction("creator"))))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }

    @Test
    void byCreatorId() {

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", new AccessByCreatorRestriction())))
              .permission("office:read")
              .build();

      final var users = officeRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class AccessByMember {

    @Test
    void byMemberRestrictionOneToMany() {
      final var restriction = new AccessByMemberRestriction<>();

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", restriction)))
              .permission("office:read")
              .build();

      final var offices = officeRepository.findAll(specification);
      assertThat(offices).hasSize(1);
    }

    @Test
    void byMemberRestrictionManyToMany() {
      final var restriction = new AccessByMemberRestriction<>("attendees");

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("office:read", restriction)))
              .permission("office:read")
              .build();

      final var offices = officeRepository.findAll(specification);
      assertThat(offices).isEmpty();
    }

    @Test
    void byMemberRestrictionCrossTable() {

      final var restriction = new AccessByMemberRestriction<>("members:member");

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("team:read", restriction)))
              .permission("team:read")
              .build();

      final var offices = teamRepository.findAll(specification);
      assertThat(offices).hasSize(2);
    }
  }

  @Nested
  class AccessByHandler {

    @Test
    void byHandlerRestriction() {
      final var restriction = new AccessByHandlersRestriction();

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);

      assertThat(users).hasSize(2);
    }

    @Test
    void byHandlerHrRestriction() {
      final var restriction = new AccessByHandlersRestriction("HR", "handlers");

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }

    @Test
    void byHandlerHrRestrictionWithId() {
      final var restriction = new AccessByHandlersRestriction("HR", "handlers:id");

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }

    @Test
    void byHandlerTeamRestriction() {
      final var restriction = new AccessByHandlersRestriction("*", "handlers:member");

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = teamRepository.findAll(specification);
      assertThat(users).hasSize(2);
    }
  }

  @Nested
  class And {
    @Test
    void byAndRestriction() {
      final var restriction =
          new AndRestriction(
              new AccessByIdRestriction<>(2L), new AccessByCreatorRestriction("creator:id"));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(1);
    }

    @Test
    void byAndRestrictionEmpty() {
      final var restriction = new AndRestriction();

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(2);
    }
  }

  @Nested
  class Or {

    @Test
    void byOrRestriction() {
      final var restriction =
          new OrRestriction(
              new AccessByIdRestriction<>(1L), new AccessByCreatorRestriction("creator:id"));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(2);
    }

    @Test
    void byOrRestrictionEmpty() {
      final var restriction = new OrRestriction();

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(specification);
      assertThat(users).hasSize(2);
    }

    @Test
    void byRelatedHandlerOrId() {
      final var restriction =
          new OrRestriction(
              new AccessByRelatedEntityRestriction(
                  "teams:team", new AccessByHandlersRestriction("OP", "handlers:member")),
              new AccessByRelatedEntityRestriction(
                  "teams:team", new AccessByHandlersRestriction("*", "handlers:member")));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var offices = userRepository.findAll(specification);
      assertThat(offices).hasSize(2);
    }
  }

  @Nested
  class AccessByRelated {
    @Test
    void byRelatedRestrictionOneToMany() {

      final var restriction =
          new AccessByRelatedEntityRestriction("office", new AccessByIdRestriction<>(1L));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var offices = userRepository.findAll(specification);
      assertThat(offices).hasSize(1);
    }

    @Test
    void byRelatedRestrictionManyToMany() {

      final var restriction =
          new AccessByRelatedEntityRestriction("teams:team", new AccessByIdRestriction<>(1L));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var offices = userRepository.findAll(specification);
      assertThat(offices).hasSize(2);
    }

    @Test
    void byRelatedIds() {
      final var restriction =
          new AccessByRelatedEntityRestriction(
              "teams:team",
              new OrRestriction(new AccessByIdRestriction<>(1L), new AccessByIdRestriction<>(2L)));

      final var specification =
          Specification.builder()
              .request(new Requester(1L, Map.of("user:read", restriction)))
              .permission("user:read")
              .build();

      final var offices = userRepository.findAll(specification);
      assertThat(offices).hasSize(2);
    }
  }

  @Nested
  class Related {

    @Test
    void findAllRelated() {
      final var specification =
          Specification.builder()
              .request(
                  Requester.builder()
                      .id(1)
                      .addPermission(
                          "office:read",
                          new AccessByRelatedEntityRestriction(
                              "members", new AccessByCreatorRestriction("creator")))
                      .build())
              .permission("office:read")
              .build();

      final var users = userRepository.findAllRelated(specification);
      assertThat(users).hasSize(1);
    }
  }
}
