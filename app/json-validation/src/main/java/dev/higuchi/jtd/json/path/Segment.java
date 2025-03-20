package dev.higuchi.jtd.json.path;

import java.util.Objects;

public class Segment {
  String value;

  Segment(String value) {
    this.value = value;
  }

  public static Segment root() {
    return new Segment("$");
  }

  public static Segment create(String selector) {
    return new Segment(selector);
  }

  public static Segment createArrayWith(Segment segment, int index) {
    return new Segment("%s[%d]".formatted(segment.value(), index));
  }

  public static Segment createArrayWithWildcard(Segment segment) {
    return new Segment("%s[*]".formatted(segment.value()));
  }

  public boolean isRoot() {
    return Objects.equals(value, "$");
  }

  public boolean isArray() {
    return value.endsWith("[*]");
  }

  public String value() {
    return value;
  }
}
