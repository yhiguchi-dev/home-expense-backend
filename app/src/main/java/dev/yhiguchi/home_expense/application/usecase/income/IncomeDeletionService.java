package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeDeleter;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
@Transactional
public class IncomeDeletionService {

  IncomeRepository incomeRepository;

  public IncomeDeletionService(IncomeRepository incomeRepository) {
    this.incomeRepository = incomeRepository;
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    Function<IncomeIdentifier, Income> getFn = identifier -> incomeRepository.get(identifier);
    Consumer<IncomeIdentifier> deleteFn = identifier -> incomeRepository.delete(identifier);
    IncomeDeleter deleter = new IncomeDeleter(getFn, deleteFn);
    deleter.delete(incomeIdentifier);
  }
}
