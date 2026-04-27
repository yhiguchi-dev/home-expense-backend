package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性名の一意性を保証する */
public class IncomeAttributeNameUniqueness {

  @FunctionalInterface
  public interface ExistsByName {
    boolean existsByName(IncomeAttributeName name);
  }

  private final ExistsByName existsByName;

  public IncomeAttributeNameUniqueness(ExistsByName existsByName) {
    this.existsByName = existsByName;
  }

  /** 新規登録時の一意性検査 */
  public void assertUniqueForRegistration(IncomeAttributeName name) {
    if (existsByName.existsByName(name)) {
      throw new IncomeAttributeAlreadyExistsException();
    }
  }

  /** 更新時の一意性検査。自分自身と name が一致する場合はスキップする */
  public void assertUniqueForUpdate(IncomeAttribute current, IncomeAttributeName newName) {
    if (current.hasSameName(newName)) {
      return;
    }
    if (existsByName.existsByName(newName)) {
      throw new IncomeAttributeAlreadyExistsException();
    }
  }
}
