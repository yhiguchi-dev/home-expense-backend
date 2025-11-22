package dev.higuchi.homeexpense.json.path;

import java.util.List;
import java.util.regex.Matcher;

public interface SelectorParser {
  static List<Selector> parse(String value) {
    for (SelectorPattern pattern : SelectorPattern.values()) {
      Matcher matcher = pattern.value.matcher(value);
      if (matcher.matches()) {
        switch (pattern) {
          case ARRAY -> {
            int index = Integer.parseInt(matcher.group(1));
            return List.of(new IndexSelector(index));
          }
          case ARRAY_WITH_NAME -> {
            String name = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2));
            return List.of(new NameSelector(name), new IndexSelector(index));
          }
          case WILDCARD -> {
            return List.of(new WildcardSelector());
          }
          case NAME -> {
            String name = matcher.group(1);
            return List.of(new NameSelector(name));
          }
        }
      }
    }
    return List.of();
  }
}
