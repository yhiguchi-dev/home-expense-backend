package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Pagination;

public class ExpenseCriteriaCreator {

  public static ExpenseSearchCriteria create(
      Integer page,
      Integer perPage,
      Integer year,
      Integer month,
      String category,
      String attributeId) {
    Pagination pagination = new Pagination(page, perPage);
    return new ExpenseSearchCriteria(pagination, year, month, category, attributeId);
  }
}
