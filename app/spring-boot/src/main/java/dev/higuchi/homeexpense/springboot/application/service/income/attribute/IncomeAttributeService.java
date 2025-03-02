package dev.higuchi.homeexpense.springboot.application.service.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeRepository;
import org.springframework.stereotype.Service;

@Service
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
