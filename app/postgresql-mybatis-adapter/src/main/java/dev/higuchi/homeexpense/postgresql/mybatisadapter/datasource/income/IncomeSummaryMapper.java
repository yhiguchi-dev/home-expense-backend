package dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IncomeSummaryMapper {

  Integer selectCount(@Param("criteria") IncomeSummaryCriteria criteria);

  List<Income> selectBy(@Param("criteria") IncomeSummaryCriteria criteria);
}
