package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseAttributeSummaryMapper {

  Integer selectCount(@Param("criteria") ExpenseAttributeSummaryCriteria criteria);

  List<ExpenseAttribute> selectBy(@Param("criteria") ExpenseAttributeSummaryCriteria criteria);
}
