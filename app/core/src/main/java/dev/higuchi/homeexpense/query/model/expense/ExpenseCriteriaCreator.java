package dev.higuchi.homeexpense.query.model.expense;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.query.model.pagination.Page;
import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import dev.higuchi.homeexpense.query.model.pagination.PerPage;
import java.util.Objects;

public class ExpenseCriteriaCreator {

  public static ExpenseSummaryCriteria create(
      Integer page,
      Integer perPage,
      Integer year,
      Integer month,
      String category,
      String attributeId) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        new ExpenseAttributeIdentifier(attributeId);
    if (Objects.nonNull(category)) {
      dev.higuchi.homeexpense.command.model.expense.ExpenseCategory expenseCategory =
          dev.higuchi.homeexpense.command.model.expense.ExpenseCategory.of(category);
      return new ExpenseSummaryCriteria(
          pagination, year, month, expenseCategory, expenseAttributeIdentifier);
    }
    return new ExpenseSummaryCriteria(pagination, year, month, expenseAttributeIdentifier);
  }
}
