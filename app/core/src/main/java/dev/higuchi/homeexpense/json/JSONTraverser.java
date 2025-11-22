package dev.higuchi.homeexpense.json;

import dev.higuchi.homeexpense.json.path.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface JSONTraverser {

  default List<JSONRepresentation> traverse(JSONRepresentation jsonRepresentation, JSONPath path) {
    return traverse(path, List.of(jsonRepresentation), 0);
  }

  default List<JSONRepresentation> traverse(
      JSONPath path, List<JSONRepresentation> jsonRepresentations, int index) {
    if (index == path.depth()) {
      return jsonRepresentations;
    }
    Selector current = path.get(index);
    List<JSONRepresentation> list = new ArrayList<>();
    for (JSONRepresentation json : jsonRepresentations) {
      List<JSONRepresentation> found = findBy(current, json);
      list.addAll(found);
    }
    return traverse(path, list, index + 1);
  }

  default List<JSONRepresentation> findBy(
      Selector selector, JSONRepresentation jsonRepresentation) {
    switch (selector) {
      case IndexSelector indexSelector -> {
        if (!jsonRepresentation.isArray()) {
          return List.of();
        }
        List<JSONRepresentation> array = jsonRepresentation.asArray();
        if (array.size() <= indexSelector.value()) {
          return List.of();
        }
        return List.of(array.get(indexSelector.value()));
      }
      case NameSelector nameSelector -> {
        if (!jsonRepresentation.isObject()) {
          return List.of();
        }
        Map<String, JSONRepresentation> object = jsonRepresentation.asObject();
        return List.of(object.getOrDefault(nameSelector.value(), JSONRepresentation.none()));
      }
      case WildcardSelector ignored -> {
        if (jsonRepresentation.isArray()) {
          return jsonRepresentation.asArray();
        }
        if (jsonRepresentation.isObject()) {
          return jsonRepresentation.asObject().values().stream().toList();
        }
        return List.of();
      }
    }
  }
}
