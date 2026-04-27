package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class IncomeAttributeSearchResultDataSource implements IncomeAttributeSearchResultQuerier {

  private final JdbcOperator jdbc;

  public IncomeAttributeSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public IncomeAttributeSearchResult find(IncomeAttributeSearchCriteria criteria) {
    int count = selectCount();
    if (count == 0) {
      return new IncomeAttributeSearchResult();
    }
    List<IncomeAttribute> list = selectBy(criteria);
    return new IncomeAttributeSearchResult(count, list);
  }

  private int selectCount() {
    return jdbc.queryForOptional(
            "SELECT COUNT(*) FROM expense.income_attribute",
            ParameterBinder.EMPTY,
            rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<IncomeAttribute> selectBy(IncomeAttributeSearchCriteria criteria) {
    String sql =
        "SELECT id, name FROM expense.income_attribute ORDER BY income_attribute.created_at OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
    return jdbc.queryForList(
        sql,
        ps -> {
          ps.setInt(1, criteria.offset());
          ps.setInt(2, criteria.perPage());
        },
        IncomeAttributeDataSource::mapIncomeAttribute);
  }
}
