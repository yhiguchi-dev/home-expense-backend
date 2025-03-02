package dev.higuchi.homeexpense.command.model.income;

/** 収入説明 */
public class Description {
  String value;

  public Description(String value) {
    this.value = value;
  }

  Description() {}

  public String value() {
    return value;
  }
}
