package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeAttributeSummaryMapper {

  Integer selectCount(@Param("criteria") IncomeAttributeSummaryCriteria criteria);

  List<IncomeAttribute> selectBy(@Param("criteria") IncomeAttributeSummaryCriteria criteria);
}
