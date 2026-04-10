package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeRegistrationService {

  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeRegistrationService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public IncomeAttributeIdentifier createAndRegister(IncomeAttributeName incomeAttributeName) {
    Function<IncomeAttributeName, Optional<IncomeAttribute>> findFn =
        name -> incomeAttributeRepository.find(name);
    Consumer<IncomeAttribute> registerFn =
        attribute -> incomeAttributeRepository.register(attribute);
    IncomeAttributeCreator creator = new IncomeAttributeCreator(findFn, registerFn);
    IncomeAttribute incomeAttribute = creator.createAndRegister(incomeAttributeName);
    return incomeAttribute.incomeAttributeIdentifier();
  }
}
