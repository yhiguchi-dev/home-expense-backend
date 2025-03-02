package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseMapper {

  void insert(@Param("expense") Expense expense);

  void insertFixedExpense(@Param("expense") Expense expense);

  void insertVariableExpense(@Param("expense") Expense expense);

  Optional<Expense> selectBy(@Param("expenseIdentifier") ExpenseIdentifier expenseIdentifier);

  Optional<List<Expense>> selectByExpenseAttribute(
      @Param("expenseAttribute") ExpenseAttribute expenseAttribute);

  void delete(@Param("expenseIdentifier") ExpenseIdentifier expenseIdentifier);
}
