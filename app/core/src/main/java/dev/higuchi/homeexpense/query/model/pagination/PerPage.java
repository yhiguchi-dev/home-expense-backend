package dev.higuchi.homeexpense.query.model.pagination;

public class PerPage {
  int value;

  public PerPage(int value) {
    this.value = value;
  }

  int value() {
    return value;
  }
}
