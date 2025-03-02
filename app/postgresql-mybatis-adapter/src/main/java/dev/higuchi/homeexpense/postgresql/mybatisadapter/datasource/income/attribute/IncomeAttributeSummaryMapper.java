package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeAttributeSummaryMapper {

  Integer selectCount(@Param("criteria") IncomeAttributeSummaryCriteria criteria);

  List<IncomeAttribute> selectBy(@Param("criteria") IncomeAttributeSummaryCriteria criteria);
}
