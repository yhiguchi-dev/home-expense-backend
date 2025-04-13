package dev.higuchi.jtd.json.path;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JSONPath {

  Selectors selectors;

  JSONPath(Selectors selectors) {
    this.selectors = selectors;
  }

  JSONPath() {
    this(new Selectors());
  }

  public static JSONPath createRoot() {
    return new JSONPath(new Selectors());
  }

  public static JSONPath parse(String path) {
    if (!path.startsWith("$")) {
      throw new IllegalArgumentException("JSON path must start with $");
    }
    String[] splitted = path.substring(1).split(Pattern.quote("."));
    List<Selector> selectors = new ArrayList<>();
    for (String value : splitted) {
      List<Selector> parsed = SelectorParser.parse(value);
      selectors.addAll(parsed);
    }
    return new JSONPath(new Selectors(selectors));
  }

  public JSONPath update(Selector... selectors) {
    Selectors updated = this.selectors.merge(selectors);
    return new JSONPath(updated);
  }

  public int depth() {
    return selectors.depth();
  }

  public Selector get(int index) {
    return selectors.get(index);
  }
}
