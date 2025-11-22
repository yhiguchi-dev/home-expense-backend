package dev.higuchi.homeexpense.json.path;

import java.util.*;
import java.util.stream.Stream;

public class Selectors implements Iterable<Selector> {
  List<Selector> values;

  Selectors(List<Selector> values) {
    this.values = values;
  }

  public Selectors() {
    this(new ArrayList<>());
  }

  public Selectors merge(Selector... selector) {
    List<Selector> list = Stream.concat(values.stream(), Arrays.stream(selector)).toList();
    return new Selectors(list);
  }

  public Selector get(int index) {
    return Optional.ofNullable(values.get(index)).orElseThrow(NoSuchElementException::new);
  }

  public int depth() {
    return values.size();
  }

  @Override
  public Iterator<Selector> iterator() {
    return values.iterator();
  }
}
