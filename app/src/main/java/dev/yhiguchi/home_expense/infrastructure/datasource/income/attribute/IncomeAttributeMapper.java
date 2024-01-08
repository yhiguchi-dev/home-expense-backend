package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeName;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeAttributeMapper {

  void insert(@Param("incomeAttribute") IncomeAttribute incomeAttribute);

  Optional<IncomeAttribute> selectBy(
      @Param("incomeAttributeIdentifier") IncomeAttributeIdentifier incomeAttributeIdentifier);

  Optional<IncomeAttribute> selectByIncomeAttributeName(
      @Param("incomeAttributeName") IncomeAttributeName incomeAttributeName);

  void delete(
      @Param("incomeAttributeIdentifier") IncomeAttributeIdentifier incomeAttributeIdentifier);
}
