package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.income.IncomeAttributeCreator;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
import dev.higuchi.homeexpense.quarkus.application.service.income.attribute.IncomeAttributeService;
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
