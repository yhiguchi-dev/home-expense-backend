package dev.higuchi.homeexpense.query.model.pagination;

public class Page {
  int value;

  public Page(int value) {
    this.value = value;
  }

  int value() {
    return value;
  }
}
