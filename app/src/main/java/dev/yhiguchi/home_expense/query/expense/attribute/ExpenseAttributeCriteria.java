package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseAttributeCriteria {
  ExpenseCategory expenseCategory;
  Pagination pagination;

  public ExpenseAttributeCriteria(Pagination pagination) {
    this.pagination = pagination;
  }

  public ExpenseAttributeCriteria(ExpenseCategory expenseCategory, Pagination pagination) {
    this.expenseCategory = expenseCategory;
    this.pagination = pagination;
  }

  public int offset() {
    return pagination.offset();
  }

  public int perPage() {
    return pagination.perPage();
  }

  public int page() {
    return pagination.currentPage();
  }

  int getPerPage() {
    return perPage();
  }

  int getOffset() {
    return offset();
  }
}
