package dev.higuchi.homeexpense.quarkus.application.usecase.income;

import dev.higuchi.homeexpense.command.income.IncomeAttributeUpdater;
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
