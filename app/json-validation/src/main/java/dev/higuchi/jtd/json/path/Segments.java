package dev.higuchi.jtd.json.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Segments implements Iterable<Segment> {
  List<Segment> values;

  Segments(List<Segment> values) {
    this.values = values;
  }

  Segments() {
    this(new ArrayList<>());
  }

  public static Segments root() {
    Segment root = Segment.root();
    ArrayList<Segment> list = new ArrayList<>();
    list.add(root);
    return new Segments(list);
  }

  public Segments addChild(Segment... segment) {
    List<Segment> list = Stream.concat(values.stream(), Arrays.stream(segment)).toList();
    return new Segments(list);
  }

  public boolean isRoot() {
    return values.stream().findFirst().map(Segment::isRoot).orElse(false);
  }

  public Segment get(int index) {
    return values.get(index);
  }

  public Segment findLast() {
    return values.getLast();
  }

  public Segments updateLast(Segment segment) {
    List<Segment> updated =
        Stream.concat(values.stream().limit(values.size() - 1), Stream.of(segment)).toList();
    return new Segments(updated);
  }

  public int depth() {
    return values.size();
  }

  @Override
  public Iterator<Segment> iterator() {
    return values.iterator();
  }
}
