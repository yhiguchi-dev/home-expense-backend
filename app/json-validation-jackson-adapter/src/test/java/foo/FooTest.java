package foo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.higuchi.jtd.json.JSONRepresentation;
import dev.higuchi.jtd.json.path.JSONPath;
import json.JacksonJSON;
import json.JacksonSerializer;
import org.junit.jupiter.api.Test;

public class FooTest {

  @Test
  public void test() throws JsonProcessingException {
    String json =
        """
                {
                  "firstName": "John",
                  "lastName" : "doe",
                  "age"      : 26,
                  "address"  : {
                    "streetAddress": "naist street",
                    "city"         : "Nara",
                    "postalCode"   : "630-0192"
                  },
                  "phoneNumbers": [
                    {
                      "type"  : "iPhone",
                      "number": "0123-4567-8888"
                    },
                    {
                      "type"  : "home",
                      "number": "0123-4567-8910"
                    },
                    { "aaa": {"type": "unti"}}
                  ]
                }
                """;
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(json);
    JacksonJSON root = JacksonJSON.createRoot(jsonNode);
    JSONRepresentation by = root.findBy(JSONPath.parse("$.address.city"));
    assertEquals("Nara", by.asString());
    JSONRepresentation asdf = root.findBy(JSONPath.parse("$.phoneNumbers[0].type"));
    assertEquals("iPhone", asdf.asString());
    JSONRepresentation asdf2 = root.findBy(JSONPath.parse("$.phoneNumbers[2].type"));
    assertTrue(asdf2.isNull());
    String json2 =
        """
            [
            {
                "name": "John",
                "age": 30,
                "city": "New York"
                },
                {
                "name": "Jane",
                "age": 25,
                "city": "Toronto"
            }
            ]
            """;
    JsonNode jsonNode1 = mapper.readTree(json2);
    JacksonJSON root1 = JacksonJSON.createRoot(jsonNode1);
    JSONRepresentation by1 = root1.findBy(JSONPath.parse("$[0].name"));
    assertEquals("John", by1.asString());
    JSONRepresentation by2 = root1.findBy(JSONPath.parse("$"));
    JacksonSerializer serializer = new JacksonSerializer(mapper);
    assertEquals("Jane", by2.stringify(serializer));
  }
}
