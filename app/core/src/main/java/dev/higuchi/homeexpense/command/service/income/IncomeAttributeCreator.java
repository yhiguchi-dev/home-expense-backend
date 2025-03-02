package dev.higuchi.homeexpense.command.service.income;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeAlreadyExistsException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
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
