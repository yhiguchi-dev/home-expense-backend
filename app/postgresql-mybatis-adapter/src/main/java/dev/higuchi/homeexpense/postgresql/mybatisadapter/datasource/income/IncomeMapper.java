package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeMapper {

  void insert(@Param("income") Income income);

  Optional<Income> selectBy(@Param("incomeIdentifier") IncomeIdentifier incomeIdentifier);

  Optional<List<Income>> selectByIncomeAttribute(
      @Param("incomeAttribute") IncomeAttribute incomeAttribute);

  void delete(@Param("incomeIdentifier") IncomeIdentifier incomeIdentifier);
}
