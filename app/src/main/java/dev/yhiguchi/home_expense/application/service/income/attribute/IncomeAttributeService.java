package dev.yhiguchi.home_expense.application.service.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeAttributeService {
  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public void register(IncomeAttribute incomeAttribute) {
    incomeAttributeRepository.register(incomeAttribute);
  }

  public void update(IncomeAttribute incomeAttribute) {
    incomeAttributeRepository.update(incomeAttribute);
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    incomeAttributeRepository.delete(incomeAttributeIdentifier);
  }

  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return incomeAttributeRepository.get(incomeAttributeIdentifier);
  }

  public IncomeAttribute find(IncomeAttributeName incomeAttributeName) {
    return incomeAttributeRepository.find(incomeAttributeName);
  }
}
