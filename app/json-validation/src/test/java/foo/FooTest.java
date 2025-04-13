package foo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

public class FooTest {

  @Test
  public void test() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json =
        """
                {
                    "properties": {
                        "incomes": {
                        "elements": {
                            "properties": {
                            "id": { "type": "string" },
                            "description": { "type": "string" },
                            "amount": {
                                "type": "int32"
                            },
                            "receive_date": { "type": "string" },
                            "income_attribute": {
                                "properties": {
                                "id": { "type": "string" },
                                "name": { "type": "string" }
                                }
                            }
                            }
                        }
                        }
                    }
                }
                """;

    JsonNode jsonNode = mapper.readTree("{\"foo\": \"bar\"}, \"asdfa\"");
    var spliterator = Spliterators.spliteratorUnknownSize(jsonNode.elements(), 0);
    String text = jsonNode.asText();
    String jsonValue = mapper.writeValueAsString(jsonNode);
    assertEquals("asdfa", jsonValue);
    StreamSupport.stream(spliterator, false)
        .forEach(
            e -> {
              assertEquals("STRING", e.getNodeType().name());
            });
  }
}
