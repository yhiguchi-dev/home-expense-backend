package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseAttributeSearchCriteria {
  ExpenseCategory expenseCategory;
  Pagination pagination;

  public ExpenseAttributeSearchCriteria(Pagination pagination) {
    this(null, pagination);
  }

  public ExpenseAttributeSearchCriteria(ExpenseCategory expenseCategory, Pagination pagination) {
    this.expenseCategory = expenseCategory;
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }
}
