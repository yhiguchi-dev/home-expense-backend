package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.Incomes;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeDeletionService {

  IncomeAttributeRepository incomeAttributeRepository;
  IncomeRepository incomeRepository;

  public IncomeAttributeDeletionService(
      IncomeAttributeRepository incomeAttributeRepository, IncomeRepository incomeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
    this.incomeRepository = incomeRepository;
  }

  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeRepository.get(identifier);
    Consumer<IncomeAttributeIdentifier> deleteFn =
        identifier -> incomeAttributeRepository.delete(identifier);
    Function<IncomeAttribute, Incomes> findIncomesFn =
        attribute -> incomeRepository.find(attribute);
    IncomeAttributeDeleter deleter = new IncomeAttributeDeleter(getFn, deleteFn, findIncomesFn);
    deleter.delete(incomeAttributeIdentifier);
  }
}
