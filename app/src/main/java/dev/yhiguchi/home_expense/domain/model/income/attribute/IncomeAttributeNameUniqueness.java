package dev.yhiguchi.home_expense.domain.model.income.attribute;

/** 収入属性名の一意性を保証する */
public class IncomeAttributeNameUniqueness {

  @FunctionalInterface
  public interface ExistsByName {
    boolean existsByName(IncomeAttributeName incomeAttributeName);
  }

  ExistsByName existsByName;

  public IncomeAttributeNameUniqueness(ExistsByName existsByName) {
    this.existsByName = existsByName;
  }

  public void assertUnique(IncomeAttributeName incomeAttributeName) {
    if (existsByName.existsByName(incomeAttributeName)) {
      throw new IncomeAttributeAlreadyExistsException();
    }
  }
}
