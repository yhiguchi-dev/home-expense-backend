package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.query.income.IncomeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeSummaryMapper {

  Integer selectCount(@Param("criteria") IncomeSummaryCriteria criteria);

  List<Income> selectBy(@Param("criteria") IncomeSummaryCriteria criteria);
}
