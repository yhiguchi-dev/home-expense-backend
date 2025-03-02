package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.command.model.income.IncomeRepository;
import dev.higuchi.homeexpense.command.model.income.Incomes;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import java.util.List;
import java.util.Optional;

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
