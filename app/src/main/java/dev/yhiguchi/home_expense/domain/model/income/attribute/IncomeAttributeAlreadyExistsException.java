package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性が既に存在する */
public class IncomeAttributeAlreadyExistsException extends RuntimeException {
  public IncomeAttributeAlreadyExistsException() {
    super();
  }
}
