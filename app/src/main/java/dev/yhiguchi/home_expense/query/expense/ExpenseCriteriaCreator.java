package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import java.util.Objects;

public class ExpenseCriteriaCreator {

  public static ExpenseSearchCriteria create(
      Integer page,
      Integer perPage,
      Integer year,
      Integer month,
      String category,
      String attributeId) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        Objects.nonNull(attributeId) ? new ExpenseAttributeIdentifier(attributeId) : null;
    if (Objects.nonNull(category)) {
      dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory expenseCategory =
          dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory.of(category);
      return new ExpenseSearchCriteria(
          pagination, year, month, expenseCategory, expenseAttributeIdentifier);
    }
    return new ExpenseSearchCriteria(pagination, year, month, expenseAttributeIdentifier);
  }
}
