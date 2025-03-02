package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAttributeMapper {

  void insert(@Param("expenseAttribute") ExpenseAttribute expenseAttribute);

  Optional<ExpenseAttribute> selectBy(
      @Param("expenseAttributeIdentifier") ExpenseAttributeIdentifier expenseAttributeIdentifier);

  Optional<ExpenseAttribute> selectByExpenseAttributeName(
      @Param("expenseAttributeName") ExpenseAttributeName expenseAttributeName);

  void delete(
      @Param("expenseAttributeIdentifier") ExpenseAttributeIdentifier expenseAttributeIdentifier);
}
