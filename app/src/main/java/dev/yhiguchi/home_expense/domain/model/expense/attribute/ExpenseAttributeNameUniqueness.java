package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;

/** 経費属性名の一意性を保証する */
public class ExpenseAttributeNameUniqueness {

  @FunctionalInterface
  public interface ExistsByName {
    boolean existsByName(ExpenseAttributeName name, ExpenseCategory category);
  }

  private final ExistsByName existsByName;

  public ExpenseAttributeNameUniqueness(ExistsByName existsByName) {
    this.existsByName = existsByName;
  }

  /** 新規登録時の一意性検査 */
  public void assertUniqueForRegistration(ExpenseAttributeName name, ExpenseCategory category) {
    if (existsByName.existsByName(name, category)) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }

  /** 更新時の一意性検査。自分自身と (name, category) が一致する場合はスキップする */
  public void assertUniqueForUpdate(
      ExpenseAttribute current, ExpenseAttributeName newName, ExpenseCategory newCategory) {
    if (current.hasSameName(newName) && current.hasSameCategory(newCategory)) {
      return;
    }
    if (existsByName.existsByName(newName, newCategory)) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }
}
