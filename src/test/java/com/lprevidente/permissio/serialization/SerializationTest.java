package com.lprevidente.permissio.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lprevidente.permissio.repository.EnableAcRepositories;
import com.lprevidente.permissio.restriction.*;
import com.lprevidente.permissio.restriction.ByHandler.Id;
import com.lprevidente.permissio.restriction.ByHandler.Type;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAcRepositories("com.lprevidente.permissio.repository")
class SerializationTest {



  private final ObjectMapper mapper;

  public SerializationTest() {
    mapper = new ObjectMapper();
  }

  @Nested
  class AccessByIdTest {
    private final String json =
        """
            {
              "@type": "byId",
              "property": "id",
              "id": 1
            }""";

    @Test
    void serialize() throws JsonProcessingException, JSONException {
      final var restriction = new ById("id", 1L);

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(ById.class);
    }
  }

  @Nested
  class AccessByCreatorTest {
    private final String json =
        """
            {
              "@type": "byCreator",
              "property": "creator"
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new ByCreator("creator");

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(ByCreator.class)
          .asInstanceOf(InstanceOfAssertFactories.type(ByCreator.class))
          .hasFieldOrPropertyWithValue("property", "creator");
    }
  }

  @Nested
  class ByMemberTest {
    private final String json =
        """
            {
              "@type": "byMember",
              "property": "members.id"
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new ByMember("members.id");

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(ByMember.class)
          .asInstanceOf(InstanceOfAssertFactories.type(ByMember.class))
          .hasFieldOrPropertyWithValue("property", "members.id");
    }
  }

  @Nested
  class ByHandlerTest {

    private final String json =
        """
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
            }""";

    @Test
    void serializeHandler() throws Exception {
      final var restriction =
          new ByHandler(new Type("*", "handlers.handler"), new Id("id", "handlers.handler"));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserializeHandler() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);

      assertThat(restriction)
          .isInstanceOf(ByHandler.class)
          .asInstanceOf(InstanceOfAssertFactories.type(ByHandler.class))
          .hasFieldOrPropertyWithValue("id", new Id("id", "handlers.handler"))
          .hasFieldOrPropertyWithValue("type", new Type("*", "handlers.handler"));
    }
  }

  @Nested
  class AndTest {
    private final String json =
        """
            {
              "@type": "and",
              "restrictions": [
                {
                  "@type": "byId",
                  "property": "id",
                  "id": 1
                },
                {
                  "@type": "byId",
                  "property": "id",
                  "id": 2
                }
              ]
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new And(new ById("id", 1L), new ById("id", 2L));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(And.class)
          .asInstanceOf(InstanceOfAssertFactories.type(And.class))
          .extracting(And::restrictions)
          .asInstanceOf(InstanceOfAssertFactories.array(Restriction[].class))
          .hasSize(2);
    }
  }

  @Nested
  class OrTest {
    private final String json =
        """
            {
              "@type": "or",
              "restrictions": [
                {
                  "@type": "byId",
                  "property": "id",
                  "id": 1
                },
                {
                  "@type": "byId",
                  "property": "id",
                  "id": 2
                }
              ]
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new Or(new ById("id", 1), new ById("id", 2));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(Or.class)
          .asInstanceOf(InstanceOfAssertFactories.type(Or.class))
          .extracting(Or::restrictions)
          .asInstanceOf(InstanceOfAssertFactories.array(Restriction[].class))
          .hasSize(2);
    }
  }

  @Nested
  class ByRelatedEntityTest {
    private final String json =
        """
            {
              "@type": "byRelatedEntity",
              "property": "team",
              "restriction": {
                "@type": "byId",
                "property": "id",
                "id": 1
              }
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new ByRelatedEntity("team", new ById("id", 1));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(ByRelatedEntity.class)
          .asInstanceOf(InstanceOfAssertFactories.type(ByRelatedEntity.class))
          .hasFieldOrPropertyWithValue("property", "team")
          .extracting(ByRelatedEntity::getRestriction)
          .asInstanceOf(InstanceOfAssertFactories.type(ById.class))
          .hasFieldOrPropertyWithValue("id", 1);
    }
  }
}
