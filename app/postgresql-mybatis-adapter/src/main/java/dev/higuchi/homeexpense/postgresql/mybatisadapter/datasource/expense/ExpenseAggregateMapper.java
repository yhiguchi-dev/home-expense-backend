package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.query.model.expense.ExpenseAggregateCriteria;
import dev.higuchi.homeexpense.query.model.expense.ExpenseAttributeAggregate;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAggregateMapper {

  Optional<Integer> selectIncomeTotalAmount(@Param("criteria") ExpenseAggregateCriteria criteria);

  Optional<Integer> selectTotalAmountByFixedCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);

  List<ExpenseAttributeAggregate> selectByFixedCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);

  Optional<Integer> selectTotalAmountByVariableCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);

  List<ExpenseAttributeAggregate> selectByVariableCategory(
      @Param("criteria") ExpenseAggregateCriteria criteria);
}
