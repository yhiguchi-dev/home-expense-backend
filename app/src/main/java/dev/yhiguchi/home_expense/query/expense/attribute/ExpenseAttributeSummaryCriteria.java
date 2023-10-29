package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseAttributeSummaryCriteria {
  ExpenseCategory expenseCategory;
  Pagination pagination;

  public ExpenseAttributeSummaryCriteria(Pagination pagination) {
    this(null, pagination);
  }

  public ExpenseAttributeSummaryCriteria(ExpenseCategory expenseCategory, Pagination pagination) {
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
