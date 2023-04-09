package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeName;
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
