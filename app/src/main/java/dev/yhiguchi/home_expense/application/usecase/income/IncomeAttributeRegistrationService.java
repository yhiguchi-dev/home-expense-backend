package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeCreator;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeRegistrationService {

  IncomeAttributeService incomeAttributeService;

  public IncomeAttributeRegistrationService(IncomeAttributeService incomeAttributeService) {
    this.incomeAttributeService = incomeAttributeService;
  }

  public IncomeAttributeIdentifier createAndRegister(IncomeAttributeName incomeAttributeName) {
    Function<IncomeAttributeName, IncomeAttribute> findFn =
        name -> incomeAttributeService.find(name);
    Consumer<IncomeAttribute> registerFn = attribute -> incomeAttributeService.register(attribute);
    IncomeAttributeCreator creator = new IncomeAttributeCreator(findFn, registerFn);
    IncomeAttribute incomeAttribute = creator.createAndRegister(incomeAttributeName);
    return incomeAttribute.incomeAttributeIdentifier();
  }
}
