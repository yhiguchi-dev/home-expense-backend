package dev.yhiguchi.home_expense.domain.model.expense.attribute;

/** 経費属性が見つからない */
public class ExpenseAttributeNotFoundException extends RuntimeException {
  public ExpenseAttributeNotFoundException() {
    super();
  }
}
