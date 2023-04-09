package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseMapper {

  void insert(@Param("expense") Expense expense);

  void insertFixedExpense(@Param("expense") Expense expense);

  void insertVariableExpense(@Param("expense") Expense expense);

  Optional<Expense> selectBy(@Param("expenseIdentifier") ExpenseIdentifier expenseIdentifier);

  void delete(@Param("expenseIdentifier") ExpenseIdentifier expenseIdentifier);
}
