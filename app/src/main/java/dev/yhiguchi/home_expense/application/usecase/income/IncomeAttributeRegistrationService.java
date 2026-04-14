package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeAttributeRegistrationService {

  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeRegistrationService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public IncomeAttributeIdentifier createAndRegister(IncomeAttributeName incomeAttributeName) {
    if (incomeAttributeRepository.existsByName(incomeAttributeName)) {
      throw new IncomeAttributeAlreadyExistsException();
    }
    IncomeAttribute incomeAttribute = IncomeAttribute.create(incomeAttributeName);
    incomeAttributeRepository.register(incomeAttribute);
    return incomeAttribute.incomeAttributeIdentifier();
  }
}
