package com.lprevidente.permissio.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lprevidente.permissio.repository.EnableAcRepositories;
import com.lprevidente.permissio.restriction.*;
import com.lprevidente.permissio.restriction.AccessByHandler.Id;
import com.lprevidente.permissio.restriction.AccessByHandler.Type;
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
      final var restriction = new AccessById("id", 1L);

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction).isInstanceOf(AccessById.class);
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
      final var restriction = new AccessByCreator("creator");

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByCreator.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByCreator.class))
          .hasFieldOrPropertyWithValue("property", "creator");
    }
  }

  @Nested
  class AccessByMemberTest {
    private final String json =
        """
            {
              "@type": "byMember",
              "property": "members.id"
            }""";

    @Test
    void serialize() throws Exception {
      final var restriction = new AccessByMember("members.id");

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByMember.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByMember.class))
          .hasFieldOrPropertyWithValue("property", "members.id");
    }
  }

  @Nested
  class AccessByHandlerTest {

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
          new AccessByHandler(new Type("*", "handlers.handler"), new Id("id", "handlers.handler"));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserializeHandler() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);

      assertThat(restriction)
          .isInstanceOf(AccessByHandler.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByHandler.class))
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
      final var restriction = new And(new AccessById("id", 1L), new AccessById("id", 2L));

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
      final var restriction = new Or(new AccessById("id", 1), new AccessById("id", 2));

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
  class AccessByRelatedEntityTest {
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
      final var restriction = new AccessByRelatedEntity("team", new AccessById("id", 1));

      final var res = mapper.writeValueAsString(restriction);
      JSONAssert.assertEquals(json, res, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
      final var restriction = mapper.readValue(json, Restriction.class);
      assertThat(restriction)
          .isInstanceOf(AccessByRelatedEntity.class)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessByRelatedEntity.class))
          .hasFieldOrPropertyWithValue("property", "team")
          .extracting(AccessByRelatedEntity::getRestriction)
          .asInstanceOf(InstanceOfAssertFactories.type(AccessById.class))
          .hasFieldOrPropertyWithValue("id", 1);
    }
  }
}
