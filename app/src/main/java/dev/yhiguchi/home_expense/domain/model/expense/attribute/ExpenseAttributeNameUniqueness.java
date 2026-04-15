package dev.yhiguchi.home_expense.domain.model.expense.attribute;

/** 経費属性名の一意性を保証する */
public class ExpenseAttributeNameUniqueness {

  @FunctionalInterface
  public interface ExistsByName {
    boolean existsByName(ExpenseAttributeName expenseAttributeName);
  }

  ExistsByName existsByName;

  public ExpenseAttributeNameUniqueness(ExistsByName existsByName) {
    this.existsByName = existsByName;
  }

  public void assertUnique(ExpenseAttributeName expenseAttributeName) {
    if (existsByName.existsByName(expenseAttributeName)) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }
}
