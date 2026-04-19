package dev.yhiguchi.home_expense.domain.model.expense.attribute;

/** 経費属性の削除可否を判定する */
public class ExpenseAttributeDeletionPolicy {

  @FunctionalInterface
  public interface ExpenseExistsByAttribute {
    boolean existsByAttributeIdentifier(ExpenseAttributeIdentifier expenseAttributeIdentifier);
  }

  ExpenseExistsByAttribute expenseExistsByAttribute;

  public ExpenseAttributeDeletionPolicy(ExpenseExistsByAttribute expenseExistsByAttribute) {
    this.expenseExistsByAttribute = expenseExistsByAttribute;
  }

  public void assertDeletable(ExpenseAttribute expenseAttribute) {
    if (expenseExistsByAttribute.existsByAttributeIdentifier(
        expenseAttribute.expenseAttributeIdentifier())) {
      throw new ExpenseAttributeConstraintException();
    }
  }
}
