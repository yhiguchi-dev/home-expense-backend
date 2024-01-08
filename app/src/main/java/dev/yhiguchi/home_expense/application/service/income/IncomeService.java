package dev.yhiguchi.home_expense.application.service.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.Incomes;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeService {

  IncomeRepository incomeRepository;

  public IncomeService(IncomeRepository incomeRepository) {
    this.incomeRepository = incomeRepository;
  }

  public void register(Income income) {
    incomeRepository.register(income);
  }

  public void update(Income income) {
    incomeRepository.update(income);
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    incomeRepository.delete(incomeIdentifier);
  }

  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeRepository.get(incomeIdentifier);
  }

  public Incomes find(IncomeAttribute incomeAttribute) {
    return incomeRepository.find(incomeAttribute);
  }
}
