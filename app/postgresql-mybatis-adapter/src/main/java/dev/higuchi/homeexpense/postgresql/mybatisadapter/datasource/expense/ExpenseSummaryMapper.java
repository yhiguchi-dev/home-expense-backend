package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExpenseSummaryMapper {

  Integer selectCount(@Param("criteria") ExpenseSummaryCriteria criteria);

  List<Expense> selectBy(@Param("criteria") ExpenseSummaryCriteria criteria);
}
