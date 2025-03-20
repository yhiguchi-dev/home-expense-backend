package dev.higuchi.jtd.json.path;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JSONPath {

  Segments segments;

  JSONPath(Segments segments) {
    this.segments = segments;
  }

  JSONPath() {
    this(new Segments());
  }

  public static JSONPath createRoot() {
    return new JSONPath(Segments.root());
  }

  public static JSONPath parse(String path) {
    JSONPath root = new JSONPath();
    String[] splitted = path.split("\\.");
    return root.addSelector(splitted);
  }

  public JSONPath addSelector(String... selector) {
    Segment[] array = Arrays.stream(selector).map(Segment::create).toArray(Segment[]::new);
    Segments updated = this.segments.addChild(array);
    return new JSONPath(updated);
  }

  public JSONPath addArraySelector(int index) {
    Segment segment = segments.findLast();
    Segment child = Segment.createArrayWith(segment, index);
    Segments updated = segments.updateLast(child);
    return new JSONPath(updated);
  }

  public JSONPath addArrayWildcardSelector() {
    Segment segment = segments.findLast();
    Segment child = Segment.createArrayWithWildcard(segment);
    Segments updated = segments.updateLast(child);
    return new JSONPath(updated);
  }

  public boolean isRoot() {
    return segments.isRoot();
  }

  public int depth() {
    return segments.depth();
  }

  public String selector(int index) {
    return segments.get(index).value();
  }

  public String stringify() {
    return StreamSupport.stream(segments.spliterator(), false)
        .map(Segment::value)
        .collect(Collectors.joining("."));
  }
}
