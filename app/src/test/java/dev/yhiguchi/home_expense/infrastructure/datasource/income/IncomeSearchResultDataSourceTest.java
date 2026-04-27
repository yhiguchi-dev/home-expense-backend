package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute.IncomeAttributeDataSource;
import dev.yhiguchi.home_expense.query.Page;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.PerPage;
import dev.yhiguchi.home_expense.query.income.IncomeSearchCriteria;
import dev.yhiguchi.home_expense.query.income.IncomeSearchResult;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class IncomeSearchResultDataSourceTest {

  @Inject IncomeSearchResultDataSource sut;

  @Inject IncomeDataSource incomeDataSource;

  @Inject IncomeAttributeDataSource incomeAttributeDataSource;

  @Inject DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.income")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.income_attribute")) {
        ps.executeUpdate();
      }
    }
  }

  @Test
  void 全件検索で結果を取得できる() {
    IncomeAttribute attribute = registerAttribute("給与");
    registerIncome("4月給与", 300000, "2026-04-25", attribute);
    registerIncome("5月給与", 310000, "2026-05-25", attribute);

    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, null);
    IncomeSearchResult result = sut.find(criteria);

    assertEquals(2, result.totalCount());
    assertEquals(2, result.list().size());
  }

  @Test
  void 年で絞り込み検索できる() {
    IncomeAttribute attribute = registerAttribute("給与2");
    registerIncome("2026年給与", 300000, "2026-04-25", attribute);
    registerIncome("2025年給与", 280000, "2025-04-25", attribute);

    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, 2026);
    IncomeSearchResult result = sut.find(criteria);

    assertEquals(1, result.totalCount());
    assertEquals("2026年給与", result.list().getFirst().description().value());
  }

  @Test
  void ページネーションが正しく動作する() {
    IncomeAttribute attribute = registerAttribute("給与3");
    for (int i = 1; i <= 5; i++) {
      registerIncome("収入" + i, i * 10000, "2026-04-%02d".formatted(i), attribute);
    }

    Pagination pagination = new Pagination(new Page(1), new PerPage(2));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, null);
    IncomeSearchResult result = sut.find(criteria);

    assertEquals(5, result.totalCount());
    assertEquals(2, result.list().size());
  }

  @Test
  void データが存在しない場合は空の結果が返る() {
    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    IncomeSearchCriteria criteria = new IncomeSearchCriteria(pagination, null);
    IncomeSearchResult result = sut.find(criteria);

    assertEquals(0, result.totalCount());
    assertTrue(result.list().isEmpty());
  }

  private IncomeAttribute registerAttribute(String name) {
    IncomeAttribute attribute = IncomeAttribute.create(new IncomeAttributeName(name));
    incomeAttributeDataSource.register(attribute);
    return attribute;
  }

  private void registerIncome(
      String description, int amount, String receiveDate, IncomeAttribute attribute) {
    Income income =
        new Income(
            new IncomeIdentifier(UUID.randomUUID().toString()),
            new Description(description),
            new Amount(amount),
            new ReceiveDate(LocalDate.parse(receiveDate)),
            attribute.incomeAttributeIdentifier());
    incomeDataSource.register(income);
  }
}
