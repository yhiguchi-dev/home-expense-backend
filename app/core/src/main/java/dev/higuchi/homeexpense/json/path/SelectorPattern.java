package dev.higuchi.homeexpense.json.path;

import java.util.regex.Pattern;

public enum SelectorPattern {
  ARRAY(Pattern.compile("^\\[(\\d+)]$")),
  ARRAY_WITH_NAME(Pattern.compile("^(.+)\\[(\\d+)]$")),
  WILDCARD(Pattern.compile("\\*")),
  NAME(Pattern.compile("(.+)"));

  final Pattern value;

  SelectorPattern(Pattern value) {
    this.value = value;
  }
}
