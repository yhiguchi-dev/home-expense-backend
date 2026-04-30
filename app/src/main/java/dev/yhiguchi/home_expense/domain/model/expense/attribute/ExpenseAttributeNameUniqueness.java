package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;

/** 経費属性名の一意性を保証する */
public class ExpenseAttributeNameUniqueness {

  private final ExpenseAttributeNameLookup lookup;

  public ExpenseAttributeNameUniqueness(ExpenseAttributeNameLookup lookup) {
    this.lookup = lookup;
  }

  /** 新規登録時の一意性検査 */
  public void assertUniqueForRegistration(ExpenseAttributeName name, ExpenseCategory category) {
    if (lookup.existsByName(name, category)) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }

  /** 更新時の一意性検査。category は不変のため自身のカテゴリで判定する。名前未変更ならスキップ。 */
  public void assertUniqueForUpdate(ExpenseAttribute current, ExpenseAttributeName newName) {
    if (current.hasSameName(newName)) {
      return;
    }
    if (lookup.existsByName(newName, current.expenseCategory())) {
      throw new ExpenseAttributeAlreadyExistsException();
    }
  }
}
