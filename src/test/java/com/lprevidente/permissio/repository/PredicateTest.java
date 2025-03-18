package com.lprevidente.permissio.repository;

import static com.lprevidente.permissio.restriction.ByHandler.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lprevidente.permissio.entity.Requester;
import com.lprevidente.permissio.restriction.*;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@EnableAcRepositories(basePackages = "com.lprevidente.permissio.repository")
@Sql(scripts = "classpath:users.sql")
class PredicateTest {

  @Autowired private UserRepository userRepository;
  @Autowired private TeamRepository teamRepository;
  @Autowired private OfficeRepository officeRepository;

  @Nested
  class VoidTest {
    private final Requester<Long> requester = new Requester<>(1L, Map.of());

    @Test
    @DisplayName("When user not has the permission then don't see anything")
    void noHasPermission() {
      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission("user:read")
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("When user no permission specified the show all")
    void noPermission() {
      final var criteria = AcCriteria.builder().request(requester).build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).isNotEmpty();
    }
  }

  @Nested
  class AccessByIdTest {

    @Test
    @DisplayName("Access by Id")
    void byId() {
      final var permission = "user:read";
      final var requester =
          Requester.builder().id("").addPermission(permission, new ById("id", 1)).build();

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class AccessByCreatorTest {

    @Test
    @DisplayName("Access by creator which is an entity")
    void byCreatorWithObj() {
      final var permission = "user:read";

      final var requester =
          Requester.builder()
              .id(1L)
              .addPermission(permission, new ByCreator("creator.id"))
              .build();

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("Access by creator which is an column")
    void byCreatorId() {
      final var permission = "user:read";

      final var requester =
          Requester.builder()
              .id(1L)
              .addPermission(permission, new ByCreator("creatorId"))
              .build();

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = officeRepository.findAll(criteria);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class ByMemberTest {

    @Test
    @DisplayName("Access by Member Restriction with relation 1->*")
    void byMemberRestrictionOneToMany() {
      final var restriction = new ByMember("members.id");
      final var permission = "office:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = officeRepository.findAll(criteria);
      assertThat(offices).hasSize(1);
    }

    @Test
    @DisplayName("Access by Member Restriction with relation *->*")
    void byMemberRestrictionManyToMany() {
      final var restriction = new ByMember("attendees.id");
      final var permission = "office:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = officeRepository.findAll(criteria);
      assertThat(offices).isEmpty();
    }

    @Test
    @DisplayName("Access by Member Restriction with custom cross table")
    void byMemberRestrictionCrossTable() {
      final var restriction = new ByMember("members.user.id");
      final var permission = "team:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = teamRepository.findAll(criteria);
      assertThat(offices).hasSize(2);
    }
  }

  @Nested
  class ByHandlerTest {

    @Test
    @DisplayName("Access by Handler Restriction of Type *")
    void byHandlerRestriction() {
      final var permission = "user:read";
      final var restriction =
          new ByHandler(
              new Type("*", "handlers.type"), new Id("id", "handlers.handler.id"));

      final var requester =
          Requester.builder()
              .id(1L) //
              .addPermission(permission, restriction)
              .build();
      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);

      assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Access by Handler Restriction of Type HR")
    void byHandlerHrRestriction() {
      final var restriction =
          new ByHandler(
              new Type("HR", "handlers.type"), //
              new Id("id", "handlers.id"));

      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class AndTest {

    @Test
    @DisplayName("When and restriction is empty then show all")
    void byAndRestrictionEmpty() {
      final var restriction = new And();
      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("When and restriction is empty then apply in and")
    void byAndRestriction() {
      final var restriction = new And(new ById("id", 2L), new ByCreator("creator.id"));

      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(1);
    }
  }

  @Nested
  class OrTest {

    @Test
    @DisplayName("When or restriction is empty then show all")
    void byOrRestrictionEmpty() {
      final var restriction = new Or();
      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("When or restriction is empty then apply in or")
    void byOrRestriction() {
      final var restriction =
          new Or(
              new ById("id", 1L), //
              new ByCreator("creator.id"));

      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var users = userRepository.findAll(criteria);
      assertThat(users).hasSize(2);
    }
  }

  @Nested
  class AccessByRelatedTest {

    @Test
    void byRelatedRestrictionOneToMany() {

      final var restriction = new ByRelatedEntity("office", new ById("id", 1L));
      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = userRepository.findAll(criteria);
      assertThat(offices).hasSize(1);
    }

    @Test
    void byRelatedRestrictionManyToMany() {
      final var restriction = new ByRelatedEntity("teams.team", new ById("id", 1L));

      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = userRepository.findAll(criteria);
      assertThat(offices).hasSize(2);
    }

    @Test
    void byRelatedIds() {
      final var restriction =
          new ByRelatedEntity(
              "teams.team", //
              new Or(new ById("id", 1L), new ById("id", 2L)));

      final var permission = "user:read";
      final var requester = new Requester<>(1L, Map.of(permission, restriction));

      final var criteria =
          AcCriteria.builder() //
              .request(requester)
              .permission(permission)
              .build();

      final var offices = userRepository.findAll(criteria);
      assertThat(offices).hasSize(2);
    }
  }
}
