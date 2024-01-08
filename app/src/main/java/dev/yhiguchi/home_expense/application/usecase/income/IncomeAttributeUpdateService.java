package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.application.service.income.attribute.IncomeAttributeService;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeUpdater;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeAttributeUpdateService {

  IncomeAttributeService incomeAttributeService;

  public IncomeAttributeUpdateService(IncomeAttributeService incomeAttributeService) {
    this.incomeAttributeService = incomeAttributeService;
  }

  public void update(
      IncomeAttributeIdentifier incomeAttributeIdentifier,
      IncomeAttributeName incomeAttributeName) {
    Function<IncomeAttributeIdentifier, IncomeAttribute> getFn =
        identifier -> incomeAttributeService.get(identifier);
    Consumer<IncomeAttribute> updateFn = attribute -> incomeAttributeService.update(attribute);
    IncomeAttributeUpdater updater = new IncomeAttributeUpdater(getFn, updateFn);
    updater.update(incomeAttributeIdentifier, incomeAttributeName);
  }
}
