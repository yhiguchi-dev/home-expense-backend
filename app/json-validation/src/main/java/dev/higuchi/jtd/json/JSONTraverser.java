package dev.higuchi.jtd.json;

import dev.higuchi.jtd.json.path.JSONPath;
import java.util.Map;
import java.util.Optional;

public class JSONTraverser {

  JSONRepresentation jsonRepresentation;

  public JSONTraverser(JSONRepresentation jsonRepresentation) {
    this.jsonRepresentation = jsonRepresentation;
  }

  public Optional<JSONRepresentation> traverse(JSONPath path) {
    if (!jsonRepresentation.isRoot()) {
      return Optional.empty();
    }
    return traverse(path, jsonRepresentation, 1);
  }

  Optional<JSONRepresentation> traverse(
      JSONPath path, JSONRepresentation jsonRepresentation, int index) {
    if (path.depth() == index) {
      return Optional.of(jsonRepresentation);
    }
    if (!jsonRepresentation.isObject()) {
      return Optional.empty();
    }
    Map<String, JSONRepresentation> map = jsonRepresentation.asObject();
    Optional<JSONRepresentation> next = Optional.ofNullable(map.get(path.selector(index)));
    if (next.isEmpty()) {
      return Optional.empty();
    }
    return traverse(path, next.get(), index + 1);
  }
}
