package dev.higuchi.jtd.json;

import dev.higuchi.jtd.json.path.JSONPath;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JSONRepresentation {

  boolean isRoot();

  boolean isNull();

  boolean isBoolean();

  boolean isNumber();

  boolean isString();

  boolean isArray();

  boolean isObject();

  boolean asBoolean();

  double asNumber();

  String asString();

  List<JSONRepresentation> asArray();

  Map<String, JSONRepresentation> asObject();

  String stringify(JSONSerializer serializer);

  <TYPE> TYPE convert(JSONParser parser, Class<TYPE> clazz);

  String jsonPath();

  default Optional<JSONRepresentation> findBy(String jsonPath) {
    JSONTraverser traverser = new JSONTraverser(this);
    JSONPath path = JSONPath.parse(jsonPath);
    return traverser.traverse(path);
  }
}
