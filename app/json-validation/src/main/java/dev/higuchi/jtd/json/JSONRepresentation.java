package dev.higuchi.jtd.json;

import dev.higuchi.jtd.json.path.JSONPath;
import java.util.List;
import java.util.Map;

public interface JSONRepresentation extends JSONTraverser {

  static JSONRepresentation none() {
    return new JSONRepresentation() {
      @Override
      public boolean exists() {
        return true;
      }
    };
  }

  default boolean exists() {
    return false;
  }

  default boolean isNull() {
    return false;
  }

  default boolean isBoolean() {
    return false;
  }

  default boolean isNumber() {
    return false;
  }

  default boolean isString() {
    return false;
  }

  default boolean isArray() {
    return false;
  }

  default boolean isObject() {
    return false;
  }

  default boolean asBoolean() {
    throw new UnsupportedOperationException();
  }

  default double asNumber() {
    throw new UnsupportedOperationException();
  }

  default String asString() {
    throw new UnsupportedOperationException();
  }

  default List<JSONRepresentation> asArray() {
    throw new UnsupportedOperationException();
  }

  default Map<String, JSONRepresentation> asObject() {
    throw new UnsupportedOperationException();
  }

  default String stringify(JSONSerializer serializer) {
    throw new UnsupportedOperationException();
  }

  default <TYPE> TYPE convert(JSONParser parser, Class<TYPE> clazz) {
    throw new UnsupportedOperationException();
  }

  default JSONPath jsonPath() {
    throw new UnsupportedOperationException();
  }

  default List<JSONRepresentation> findBy(JSONPath path) {
    return traverse(this, path);
  }

  default JSONRepresentation findFirstBy(JSONPath path) {
    List<JSONRepresentation> found = findBy(path);
    if (found.isEmpty()) {
      return none();
    }
    return found.getFirst();
  }
}
