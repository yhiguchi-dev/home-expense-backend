package dev.higuchi.homeexpense.command.model.income;

/** 金額 */
public class Amount {
  int value;

  public Amount(int value) {
    this.value = value;
  }

  Amount() {}

  public int value() {
    return value;
  }
}
