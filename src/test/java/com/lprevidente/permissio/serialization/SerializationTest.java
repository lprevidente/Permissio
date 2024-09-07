package com.lprevidente.permissio.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lprevidente.permissio.repository.EnableAcRepositories;
import com.lprevidente.permissio.restrictions.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAcRepositories("com.lprevidente.permissio.repository")
class SerializationTest {

  @Nested
  class AccessById {
    private final String json =
        """
            {"@type":"accessById","id":1}""";

    @Test
    void serialize() throws JsonProcessingException {
      final var restriction = new AccessByIdRestriction(1);

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessByIdRestriction.class);
    }
  }

  @Nested
  class AccessByCreator {
    private final String json =
        """
            {"@type":"accessByCreator","property":"creator"}""";

    @Test
    void serialize() throws Exception {
      final var restriction = new AccessByCreatorRestriction("creator");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByCreatorRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByCreatorRestriction.class))
          .hasFieldOrPropertyWithValue("property", "creator");
    }
  }

  @Nested
  class AccessByMember {
    private final String json =
        """
            {"@type":"accessByMember","property":"members:id"}""";

    @Test
    void serialize() throws Exception {
      final var restriction = new AccessByMemberRestriction("members:id");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByMemberRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByMemberRestriction.class))
          .hasFieldOrPropertyWithValue("property", "members:id");
    }
  }

  @Nested
  class AccessByHandler {

    private final String json =
        """
            {"@type":"accessByHandlers","property":"handlers","type":"OP"}""";

    @Test
    void serializeHandler() throws Exception {
      final var restriction = new AccessByHandlersRestriction("OP", "handlers");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserializeHandler() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);

      assertThat(restriction)
          .isInstanceOf(AccessByHandlersRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByHandlersRestriction.class))
          .hasFieldOrPropertyWithValue("property", "handlers")
          .hasFieldOrPropertyWithValue("type", "OP");
    }
  }

  @Nested
  class And {
    private final String json =
        """
            {"@type":"and","restrictions":[{"@type":"accessById","id":1},{"@type":"accessById","id":2}]}""";

    @Test
    void serialize() throws Exception {
      final var restriction =
          new AndRestriction(new AccessByIdRestriction(1), new AccessByIdRestriction(2));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AndRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AndRestriction.class))
          .extracting(AndRestriction::getRestrictions)
          .asInstanceOf(InstanceOfAssertFactories.array(Restriction[].class))
          .hasSize(2);
    }
  }

  @Nested
  class Or {
    private final String json =
        """
            {"@type":"or","restrictions":[{"@type":"accessById","id":1},{"@type":"accessById","id":2}]}""";

    @Test
    void serialize() throws Exception {
      final var restriction =
          new OrRestriction(new AccessByIdRestriction(1), new AccessByIdRestriction(2));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(OrRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(OrRestriction.class))
          .extracting(OrRestriction::getRestrictions)
          .asInstanceOf(InstanceOfAssertFactories.array(Restriction[].class))
          .hasSize(2);
    }
  }

  @Nested
  class AccessByRelatedEntity {
    private final String json =
        """
            {"@type":"accessByRelatedEntity","property":"team","restriction":{"@type":"accessById","id":1}}""";

    @Test
    void serialize() throws Exception {
      final var restriction =
          new AccessByRelatedEntityRestriction("team", new AccessByIdRestriction(1));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByRelatedEntityRestriction.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByRelatedEntityRestriction.class))
          .hasFieldOrPropertyWithValue("property", "team")
          .extracting(AccessByRelatedEntityRestriction::getRestriction)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByIdRestriction.class))
          .hasFieldOrPropertyWithValue("id", 1L);
    }
  }
}
