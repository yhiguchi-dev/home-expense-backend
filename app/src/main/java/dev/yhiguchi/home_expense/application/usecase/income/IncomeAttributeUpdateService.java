package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeUpdateService {

  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeUpdateService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public void update(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeRepository.get(identifier);
    Consumer<IncomeAttribute> updateFn = attribute -> incomeAttributeRepository.update(attribute);
    IncomeAttributeUpdater updater = new IncomeAttributeUpdater(getFn, updateFn);
    updater.update(incomeAttributeIdentifier, incomeAttributeName);
  }
}
