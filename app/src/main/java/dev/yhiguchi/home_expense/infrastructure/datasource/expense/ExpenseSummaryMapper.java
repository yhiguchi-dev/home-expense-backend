package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.query.expense.ExpenseCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseSummaryMapper {

  Integer selectCount(@Param("expenseCriteria") ExpenseCriteria expenseCriteria);

  List<Expense> selectBy(@Param("expenseCriteria") ExpenseCriteria expenseCriteria);
}
