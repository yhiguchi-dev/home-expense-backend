package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseAttributeSearchCriteria {
  String expenseCategory;
  Pagination pagination;

  public ExpenseAttributeSearchCriteria(Pagination pagination) {
    this(null, pagination);
  }

  public ExpenseAttributeSearchCriteria(String expenseCategory, Pagination pagination) {
    this.expenseCategory = expenseCategory;
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public String expenseCategory() {
    return expenseCategory;
  }
}
