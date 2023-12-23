package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性が見つからない */
public class IncomeAttributeNotFoundException extends RuntimeException {
  public IncomeAttributeNotFoundException(String message) {
    super(message);
  }
}
