package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import dev.yhiguchi.home_expense.domain.model.income.Incomes;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IncomeDataSource implements IncomeRepository {
  IncomeMapper incomeMapper;

  public IncomeDataSource(IncomeMapper incomeMapper) {
    this.incomeMapper = incomeMapper;
  }

  @Override
  public void register(Income income) {
    incomeMapper.insert(income);
  }

  @Override
  public void update(Income income) {
    delete(income.incomeIdentifier());
    register(income);
  }

  @Override
  public void delete(IncomeIdentifier incomeIdentifier) {
    incomeMapper.delete(incomeIdentifier);
  }

  @Override
  public Income get(IncomeIdentifier incomeIdentifier) {
    return incomeMapper.selectBy(incomeIdentifier).orElseThrow();
  }

  @Override
  public Incomes find(IncomeAttribute incomeAttribute) {
    Optional<List<Income>> incomes = incomeMapper.selectByIncomeAttribute(incomeAttribute);
    return incomes.map(Incomes::new).orElseGet(Incomes::new);
  }
}
