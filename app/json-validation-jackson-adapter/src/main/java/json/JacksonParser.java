package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.higuchi.jtd.json.JSONParser;

public class JacksonParser implements JSONParser {

  ObjectMapper objectMapper;

  public JacksonParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <TYPE> TYPE parse(Object json, Class<TYPE> type) {
    return objectMapper.convertValue(json, type);
  }
}
