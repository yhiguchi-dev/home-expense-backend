package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.Optional;

/** 経費属性リポジトリ */
public interface ExpenseAttributeRepository {
  void register(ExpenseAttribute expenseAttribute);

  Optional<Revision<ExpenseAttribute>> findBy(
      ExpenseAttributeIdentifier expenseAttributeIdentifier);

  boolean existsByName(ExpenseAttributeName expenseAttributeName, ExpenseCategory expenseCategory);

  void update(Revision<ExpenseAttribute> expenseAttribute);

  void delete(ExpenseAttribute expenseAttribute);
}
