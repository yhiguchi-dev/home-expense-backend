package dev.yhiguchi.home_expense.domain.model.income.attribute;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/** 収入属性作成者 */
public class IncomeAttributeCreator {

  Function<IncomeAttributeName, IncomeAttribute> findFn;
  Consumer<IncomeAttribute> registerFn;

  public IncomeAttributeCreator(
      Function<IncomeAttributeName, IncomeAttribute> findFn, Consumer<IncomeAttribute> registerFn) {
    this.findFn = findFn;
    this.registerFn = registerFn;
  }

  public IncomeAttribute createAndRegister(IncomeAttributeName incomeAttributeName) {
    throwIfIncomeAttributeAlreadyExists(incomeAttributeName);
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        new IncomeAttributeIdentifier(UUID.randomUUID().toString());
    IncomeAttribute incomeAttribute =
        new IncomeAttribute(incomeAttributeIdentifier, incomeAttributeName);
    registerFn.accept(incomeAttribute);
    return incomeAttribute;
  }

  void throwIfIncomeAttributeAlreadyExists(IncomeAttributeName incomeAttributeName) {
    IncomeAttribute incomeAttribute = findFn.apply(incomeAttributeName);
    if (incomeAttribute.exists()) {
      throw new IncomeAttributeAlreadyExistsException();
    }
  }
}
