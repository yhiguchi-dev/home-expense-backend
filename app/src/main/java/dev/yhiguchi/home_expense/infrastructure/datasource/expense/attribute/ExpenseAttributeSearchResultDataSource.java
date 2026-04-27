package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.JdbcOperator;
import dev.yhiguchi.home_expense.infrastructure.datasource.jdbc.ParameterBinder;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResult;
import dev.yhiguchi.home_expense.query.expense.attribute.ExpenseAttributeSearchResultQuerier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@ApplicationScoped
@Transactional
public class ExpenseAttributeSearchResultDataSource implements ExpenseAttributeSearchResultQuerier {

  private final JdbcOperator jdbc;

  public ExpenseAttributeSearchResultDataSource(
      @io.quarkus.agroal.DataSource("readonly") DataSource dataSource) {
    this.jdbc = new JdbcOperator(dataSource);
  }

  @Override
  public ExpenseAttributeSearchResult find(ExpenseAttributeSearchCriteria criteria) {
    int count = selectCount(criteria);
    if (count == 0) {
      return new ExpenseAttributeSearchResult();
    }
    List<ExpenseAttribute> list = selectBy(criteria);
    return new ExpenseAttributeSearchResult(count, list);
  }

  private int selectCount(ExpenseAttributeSearchCriteria criteria) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM expense.attribute");
    List<Object> params = new ArrayList<>();
    appendCategoryFilter(criteria, sql, params);
    return jdbc.queryForOptional(sql.toString(), ParameterBinder.of(params), rs -> rs.getInt(1))
        .orElse(0);
  }

  private List<ExpenseAttribute> selectBy(ExpenseAttributeSearchCriteria criteria) {
    StringBuilder sql = new StringBuilder("SELECT id, category, name FROM expense.attribute");
    List<Object> params = new ArrayList<>();
    appendCategoryFilter(criteria, sql, params);
    sql.append(" ORDER BY attribute.created_at OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
    params.add(criteria.offset());
    params.add(criteria.perPage());
    return jdbc.queryForList(
        sql.toString(),
        ParameterBinder.of(params),
        ExpenseAttributeDataSource::mapExpenseAttribute);
  }

  private void appendCategoryFilter(
      ExpenseAttributeSearchCriteria criteria, StringBuilder sql, List<Object> params) {
    if (criteria.getExpenseCategory() != null) {
      sql.append(" WHERE attribute.category = ?");
      params.add(criteria.getExpenseCategory().name());
    }
  }
}
