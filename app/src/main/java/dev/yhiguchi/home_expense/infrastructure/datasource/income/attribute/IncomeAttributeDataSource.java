package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class IncomeAttributeDataSource implements IncomeAttributeRepository {

  IncomeAttributeMapper incomeAttributeMapper;

  public IncomeAttributeDataSource(IncomeAttributeMapper incomeAttributeMapper) {
    this.incomeAttributeMapper = incomeAttributeMapper;
  }

  @Override
  public void register(IncomeAttribute incomeAttribute) {
    incomeAttributeMapper.insert(incomeAttribute);
  }

  @Override
  public void update(IncomeAttribute incomeAttribute) {}

  @Override
  public void delete(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    incomeAttributeMapper.delete(incomeAttributeIdentifier);
  }

  @Override
  public IncomeAttribute get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    Optional<IncomeAttribute> incomeAttribute =
        incomeAttributeMapper.selectBy(incomeAttributeIdentifier);
    return incomeAttribute.orElseThrow(() -> new IncomeAttributeNotFoundException(""));
  }
}
