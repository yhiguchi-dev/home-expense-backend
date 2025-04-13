package dev.higuchi.jtd.json;

import dev.higuchi.jtd.json.path.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JSONTraverser {

  static JSONRepresentation traverse(JSONRepresentation jsonRepresentation, JSONPath path) {
    return traverse(path, jsonRepresentation, 0);
  }

  static JSONRepresentation traverse(JSONPath path, JSONRepresentation jsonRepresentation, int index) {
    if (index == path.depth()) {
      return jsonRepresentation;
    }
    Selector current = path.get(index);
    Optional<JSONRepresentation> found = findBy(current, jsonRepresentation);
    if (found.isEmpty()) {
      return JSONRepresentation.none();
    }
    return traverse(path, found.get(), index + 1);
  }

  static Optional<JSONRepresentation> findBy(Selector selector, JSONRepresentation jsonRepresentation) {
    switch (selector) {
      case IndexSelector indexSelector -> {
        if (!jsonRepresentation.isArray()) {
          return Optional.empty();
        }
        List<JSONRepresentation> array = jsonRepresentation.asArray();
        if (array.size() <= indexSelector.value()) {
          return Optional.empty();
        }
        return Optional.ofNullable(array.get(indexSelector.value()));
      }
      case NameSelector nameSelector -> {
        if (!jsonRepresentation.isObject()) {
          return Optional.empty();
        }
        Map<String, JSONRepresentation> object = jsonRepresentation.asObject();
        return Optional.ofNullable(object.get(nameSelector.value()));
      }
        case WildcardSelector wildcardSelector -> {
//            if (jsonRepresentation.isArray()) {
//            return Optional.of(jsonRepresentation);
//            } else if (jsonRepresentation.isObject()) {
//            return Optional.of(jsonRepresentation);
//            } else {
//            return Optional.empty();
//            }
          return Optional.empty();
        }
    }
  }
}
