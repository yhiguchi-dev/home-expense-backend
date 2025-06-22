package json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.higuchi.jtd.json.JSONParser;
import dev.higuchi.jtd.json.JSONRepresentation;
import dev.higuchi.jtd.json.JSONSerializer;
import dev.higuchi.jtd.json.path.IndexSelector;
import dev.higuchi.jtd.json.path.JSONPath;
import dev.higuchi.jtd.json.path.NameSelector;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JacksonJSON implements JSONRepresentation {
  JsonNode jsonNode;
  JSONPath jsonPath;

  JacksonJSON(JsonNode jsonNode, JSONPath jsonPath) {
    this.jsonNode = jsonNode;
    this.jsonPath = jsonPath;
  }

  public static JacksonJSON createRoot(JsonNode jsonNode) {
    JSONPath path = JSONPath.createRoot();
    return new JacksonJSON(jsonNode, path);
  }

  static JacksonJSON createChild(JsonNode jsonNode, JSONPath jsonPath) {
    return new JacksonJSON(jsonNode, jsonPath);
  }

  @Override
  public boolean exists() {
    return Objects.nonNull(jsonNode);
  }

  @Override
  public boolean isNull() {
    return jsonNode.isNull();
  }

  @Override
  public boolean isBoolean() {
    return jsonNode.isBoolean();
  }

  @Override
  public boolean isNumber() {
    return jsonNode.isNumber();
  }

  @Override
  public boolean isString() {
    return jsonNode.isTextual();
  }

  @Override
  public boolean isArray() {
    return jsonNode.isArray();
  }

  @Override
  public boolean isObject() {
    return jsonNode.isObject();
  }

  @Override
  public boolean asBoolean() {
    return jsonNode.asBoolean();
  }

  @Override
  public double asNumber() {
    return jsonNode.asDouble();
  }

  @Override
  public String asString() {
    return jsonNode.asText();
  }

  @Override
  public List<JSONRepresentation> asArray() {
    List<JSONRepresentation> list = new ArrayList<>(jsonNode.size());
    for (int i = 0; i < jsonNode.size(); i++) {
      JSONPath updatedPath = jsonPath.update(new IndexSelector(i));
      list.add(createChild(jsonNode.get(i), updatedPath));
    }
    return list;
  }

  @Override
  public Map<String, JSONRepresentation> asObject() {
    var spliterator = Spliterators.spliteratorUnknownSize(jsonNode.fields(), 0);
    return StreamSupport.stream(spliterator, false)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                  NameSelector nameSelector = new NameSelector(e.getKey());
                  JSONPath updatedPath = jsonPath.update(nameSelector);
                  return createChild(e.getValue(), updatedPath);
                }));
  }

  @Override
  public String stringify(JSONSerializer serializer) {
    return serializer.serialize(jsonNode);
  }

  @Override
  public <TYPE> TYPE convert(JSONParser parser, Class<TYPE> clazz) {
    return parser.parse(jsonNode, clazz);
  }

  @Override
  public JSONPath jsonPath() {
    return jsonPath;
  }
}
