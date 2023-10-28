package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.query.expense.ExpenseAggregateCriteria;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAggregateMapper {

  Optional<Integer> selectTotalAmountByFixedCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);

  List<Expense> selectByFixedCategory(@Param("criteria") ExpenseAggregateCriteria criteria);

  Optional<Integer> selectTotalAmountByVariableCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);

  List<Expense> selectByVariableCategory(@Param("criteria") ExpenseAggregateCriteria criteria);
}
