package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeDataSource implements IncomeRepository {
  @Override
  public void register(Income income) {}

  @Override
  public void update(Income income) {}

  @Override
  public void delete(IncomeIdentifier incomeIdentifier) {}

  @Override
  public Income get(IncomeIdentifier incomeIdentifier) {
    return null;
  }
}
