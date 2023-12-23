package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeAttributeMapper {

  void insert(@Param("incomeAttribute") IncomeAttribute incomeAttribute);

  Optional<IncomeAttribute> selectBy(
      @Param("incomeAttributeIdentifier") IncomeAttributeIdentifier incomeAttributeIdentifier);

  Optional<List<Expense>> selectByExpenseAttribute(
      @Param("incomeAttribute") IncomeAttribute incomeAttribute);

  void delete(
      @Param("incomeAttributeIdentifier") IncomeAttributeIdentifier incomeAttributeIdentifier);
}
