package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeUpdateService {

  IncomeRepository incomeRepository;
  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeUpdateService(
      IncomeRepository incomeRepository, IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeRepository = incomeRepository;
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public void update(
      IncomeIdentifier incomeIdentifier,
      Description description,
      Amount price,
      ReceiveDate receiveDate,
      IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Function<IncomeIdentifier, Income> getFn = identifier -> incomeRepository.get(identifier);
    Function<IncomeAttributeIdentifier, IncomeAttribute> getAttributeFn =
        identifier -> incomeAttributeRepository.get(identifier);
    Consumer<Income> updateFn = income -> incomeRepository.update(income);
    IncomeUpdater updater = new IncomeUpdater(getFn, getAttributeFn, updateFn);
    updater.update(incomeIdentifier, description, price, receiveDate, incomeAttributeIdentifier);
  }
}
