package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性の削除可否を判定する */
public class IncomeAttributeDeletionPolicy {

  @FunctionalInterface
  public interface IncomeExistsByAttribute {
    boolean existsByAttributeIdentifier(IncomeAttributeIdentifier incomeAttributeIdentifier);
  }

  IncomeExistsByAttribute incomeExistsByAttribute;

  public IncomeAttributeDeletionPolicy(IncomeExistsByAttribute incomeExistsByAttribute) {
    this.incomeExistsByAttribute = incomeExistsByAttribute;
  }

  public void assertDeletable(IncomeAttribute incomeAttribute) {
    if (incomeExistsByAttribute.existsByAttributeIdentifier(
        incomeAttribute.incomeAttributeIdentifier())) {
      throw new IncomeAttributeConstraintException();
    }
  }
}
