package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAttributeSummaryMapper {

  Integer selectCount(@Param("criteria") ExpenseAttributeSummaryCriteria criteria);

  List<ExpenseAttribute> selectBy(@Param("criteria") ExpenseAttributeSummaryCriteria criteria);
}
