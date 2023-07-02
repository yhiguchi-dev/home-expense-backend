package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.query.Pagination;
import java.util.Objects;

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
    return pagination.page();
  }

  public boolean hasExpenseCategory() {
    return Objects.isNull(expenseCategory);
  }

  public int getPerPage() {
    return perPage();
  }

  public int getOffset() {
    return offset();
  }

}
