package com.lprevidente.permissio.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lprevidente.permissio.repository.EnableAcRepositories;
import com.lprevidente.permissio.restrictions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    void serialize() throws JsonProcessingException {
      final var restriction = new AccessByCreatorRestriction("creator");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessByCreatorRestriction.class);
      assertThat(((AccessByCreatorRestriction) restriction).getProperty()).isEqualTo("creator");
    }
  }

  @Nested
  class AccessByMember {
    private final String json =
        """
            {"@type":"accessByMember","property":"members"}""";

    @Test
    void serialize() throws JsonProcessingException {
      final var restriction = new AccessByMemberRestriction("members");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessByMemberRestriction.class);
    }
  }

  @Nested
  class AccessByHandler {

    private final String json =
        """
            {"@type":"accessByHandlers","property":"handlers","type":"OP"}""";

    @Test
    void serializeHandler() throws JsonProcessingException {
      final var restriction = new AccessByHandlersRestriction("OP", "handlers");

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserializeHandler() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessByHandlersRestriction.class);
    }
  }

  @Nested
  class And {
    private final String json =
        """
            {"@type":"and","restrictions":[{"@type":"accessById","id":1},{"@type":"accessById","id":2}]}""";

    @Test
    void serialize() throws JsonProcessingException {
      final var restriction =
          new AndRestriction(new AccessByIdRestriction(1), new AccessByIdRestriction(2));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AndRestriction.class);
    }
  }

  @Nested
  class Or {
    private final String json =
        "{\"@type\":\"or\",\"restrictions\":[{\"@type\":\"accessById\",\"id\":1},{\"@type\":\"accessById\",\"id\":2}]}";

    @Test
    void serialize() throws JsonProcessingException {
      final var restriction =
          new OrRestriction(new AccessByIdRestriction(1), new AccessByIdRestriction(2));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(OrRestriction.class);
    }
  }

  @Nested
  class AccessByRelatedEntity {
    private final String json =
        """
            {"@type":"accessByRelatedEntity","property":"team","restriction":{"@type":"accessById","id":1}}""";

    @Test
    void serialize() throws JsonProcessingException {
      final var restriction =
          new AccessByRelatedEntityRestriction("team", new AccessByIdRestriction(1));

      final var res = new ObjectMapper().writeValueAsString(restriction);
      assertThat(res).isEqualTo(json);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = new ObjectMapper().readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessByRelatedEntityRestriction.class);
    }
  }
}
