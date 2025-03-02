package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeName;
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
