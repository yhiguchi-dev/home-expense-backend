package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import java.util.Objects;

public class ExpenseCriteriaCreator {

  public static ExpenseSummaryCriteria create(
      Integer page, Integer perPage, Integer year, Integer month, String category) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    if (Objects.nonNull(category)) {
      dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory expenseCategory =
          dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category);
      return new ExpenseSummaryCriteria(pagination, year, month, expenseCategory);
    }
    return new ExpenseSummaryCriteria(pagination, year, month);
  }
}
