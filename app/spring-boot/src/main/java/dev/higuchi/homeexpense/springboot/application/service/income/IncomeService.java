package dev.higuchi.homeexpense.springboot.application.service.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.command.model.income.IncomeRepository;
import dev.higuchi.homeexpense.command.model.income.Incomes;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import org.springframework.stereotype.Service;

@Service
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
