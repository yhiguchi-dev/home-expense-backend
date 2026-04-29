package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;

public class ExpenseCriteriaCreator {

  public static ExpenseSearchCriteria create(
      Integer page,
      Integer perPage,
      Integer year,
      Integer month,
      String category,
      String attributeId) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    return new ExpenseSearchCriteria(pagination, year, month, category, attributeId);
  }
}
