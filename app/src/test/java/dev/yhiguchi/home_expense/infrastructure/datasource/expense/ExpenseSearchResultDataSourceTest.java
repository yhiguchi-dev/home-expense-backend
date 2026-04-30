package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute.ExpenseAttributeDataSource;
import dev.yhiguchi.home_expense.query.Pagination;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchCriteria;
import dev.yhiguchi.home_expense.query.expense.ExpenseSearchResult;
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
class ExpenseSearchResultDataSourceTest {

  @Inject ExpenseSearchResultDataSource sut;

  @Inject ExpenseDataSource expenseDataSource;

  @Inject ExpenseAttributeDataSource expenseAttributeDataSource;

  @Inject DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.expense")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.attribute")) {
        ps.executeUpdate();
      }
    }
  }

  @Test
  void 全件検索で結果を取得できる() {
    ExpenseAttribute attribute = registerAttribute("食費", ExpenseCategory.変動費);
    registerExpense("ランチ", 1000, "2026-04-10", attribute);
    registerExpense("ディナー", 3000, "2026-04-10", attribute);

    Pagination pagination = new Pagination(1, 10);
    ExpenseSearchCriteria criteria = noFilterCriteria(pagination);
    ExpenseSearchResult result = sut.search(criteria);

    assertEquals(2, result.totalCount());
    assertEquals(2, result.list().size());
  }

  @Test
  void 年月で絞り込み検索できる() {
    ExpenseAttribute attribute = registerAttribute("食費", ExpenseCategory.変動費);
    registerExpense("4月ランチ", 1000, "2026-04-10", attribute);
    registerExpense("5月ランチ", 1200, "2026-05-10", attribute);

    Pagination pagination = new Pagination(1, 10);
    ExpenseSearchCriteria criteria = yearMonthCriteria(pagination, 2026, 4);
    ExpenseSearchResult result = sut.search(criteria);

    assertEquals(1, result.totalCount());
    assertEquals("4月ランチ", result.list().getFirst().description());
  }

  @Test
  void カテゴリで絞り込み検索できる() {
    ExpenseAttribute fixedAttr = registerAttribute("家賃", ExpenseCategory.固定費);
    ExpenseAttribute varAttr = registerAttribute("食費2", ExpenseCategory.変動費);
    registerExpense("4月家賃", 80000, "2026-04-01", fixedAttr);
    registerExpense("ランチ", 1000, "2026-04-10", varAttr);

    Pagination pagination = new Pagination(1, 10);
    ExpenseSearchCriteria criteria = categoryCriteria(pagination, ExpenseCategory.固定費);
    ExpenseSearchResult result = sut.search(criteria);

    assertEquals(1, result.totalCount());
    assertEquals("4月家賃", result.list().getFirst().description());
  }

  @Test
  void ページネーションが正しく動作する() {
    ExpenseAttribute attribute = registerAttribute("食費3", ExpenseCategory.変動費);
    for (int i = 1; i <= 5; i++) {
      registerExpense("経費" + i, i * 100, "2026-04-%02d".formatted(i), attribute);
    }

    Pagination pagination = new Pagination(1, 2);
    ExpenseSearchCriteria criteria = noFilterCriteria(pagination);
    ExpenseSearchResult result = sut.search(criteria);

    assertEquals(5, result.totalCount());
    assertEquals(2, result.list().size());
  }

  @Test
  void データが存在しない場合は空の結果が返る() {
    Pagination pagination = new Pagination(1, 10);
    ExpenseSearchCriteria criteria = noFilterCriteria(pagination);
    ExpenseSearchResult result = sut.search(criteria);

    assertEquals(0, result.totalCount());
    assertTrue(result.list().isEmpty());
  }

  private ExpenseSearchCriteria noFilterCriteria(Pagination pagination) {
    return new ExpenseSearchCriteria(pagination, null, null, null);
  }

  private ExpenseSearchCriteria yearMonthCriteria(Pagination pagination, int year, int month) {
    return new ExpenseSearchCriteria(pagination, year, month, null);
  }

  private ExpenseSearchCriteria categoryCriteria(Pagination pagination, ExpenseCategory category) {
    return new ExpenseSearchCriteria(pagination, null, null, category.name(), null);
  }

  private ExpenseAttribute registerAttribute(String name, ExpenseCategory category) {
    ExpenseAttribute attribute = ExpenseAttribute.create(new ExpenseAttributeName(name), category);
    expenseAttributeDataSource.register(attribute);
    return attribute;
  }

  private void registerExpense(
      String description, int price, String paymentDate, ExpenseAttribute attribute) {
    Expense expense =
        new Expense(
            new ExpenseIdentifier(UUID.randomUUID().toString()),
            new Description(description),
            new Amount(price),
            new PaymentDate(LocalDate.parse(paymentDate)),
            attribute.expenseAttributeIdentifier());
    expenseDataSource.register(expense);
  }
}
