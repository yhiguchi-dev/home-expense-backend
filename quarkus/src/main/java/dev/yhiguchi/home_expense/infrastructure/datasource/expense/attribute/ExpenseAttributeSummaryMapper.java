package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAttributeSummaryMapper {

  Integer selectCount(
      @Param("expenseAttributeCriteria") ExpenseAttributeCriteria expenseAttributeCriteria);

  List<ExpenseAttribute> selectByPage(
      @Param("expenseAttributeCriteria") ExpenseAttributeCriteria expenseAttributeCriteria);

  List<ExpenseAttribute> selectBy(
      @Param("expenseAttributeCriteria") ExpenseAttributeCriteria expenseAttributeCriteria);
}
