package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.infrastructure.datasource.ReadOnly;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeDetail;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResult;
import dev.yhiguchi.home_expense.query.income.attribute.IncomeAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IncomeAttributeSearchResultDataSource implements IncomeAttributeSearchResultQuerier {

  private final JdbcOperator jdbc;

  public IncomeAttributeSearchResultDataSource(@ReadOnly JdbcOperator jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public IncomeAttributeSearchResult search(IncomeAttributeSearchCriteria criteria) {
    int count = selectCount();
    if (count == 0) {
      return IncomeAttributeSearchResult.empty();
    }
    List<IncomeAttributeDetail> list = selectBy(criteria);
    return new IncomeAttributeSearchResult(count, list);
  }

  @Override
  public Optional<IncomeAttributeDetail> find(IncomeAttributeIdentifier id) {
    return jdbc.queryForOptional(
        "SELECT id, name, version FROM expense.income_attribute WHERE id = ?",
        ParameterBinder.of(id.value()),
        IncomeAttributeSearchResultDataSource::mapDetail);
  }

  private int selectCount() {
    return jdbc.queryForOptional(
            "SELECT COUNT(*) FROM expense.income_attribute",
            ParameterBinder.EMPTY,
            rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<IncomeAttributeDetail> selectBy(IncomeAttributeSearchCriteria criteria) {
    String sql =
        "SELECT id, name, version FROM expense.income_attribute ORDER BY income_attribute.created_at OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
    return jdbc.queryForList(
        sql,
        ParameterBinder.of(criteria.offset(), criteria.perPage()),
        IncomeAttributeSearchResultDataSource::mapDetail);
  }

  static IncomeAttributeDetail mapDetail(ResultSet rs) throws SQLException {
    return new IncomeAttributeDetail(
        rs.getString("id"), rs.getString("name"), rs.getLong("version"));
  }
}
