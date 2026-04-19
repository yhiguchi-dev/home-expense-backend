package dev.yhiguchi.home_expense.infrastructure.datasource.income;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.income.*;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute.IncomeAttributeDataSource;
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
class IncomeDataSourceTest {

  @Inject IncomeDataSource sut;

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
  void 収入を登録して取得できる() {
    IncomeAttribute attribute = registerAttribute("給与");
    Income income = createIncome("4月給与", 300000, "2026-04-25", attribute);
    sut.register(income);

    Income result = sut.findBy(income.incomeIdentifier()).orElseThrow();
    assertEquals(income.incomeIdentifier(), result.incomeIdentifier());
    assertEquals("4月給与", result.description().value());
    assertEquals(300000, result.amount().value());
    assertEquals(attribute.incomeAttributeIdentifier(), result.incomeAttributeIdentifier());
  }

  @Test
  void 存在しないIDで取得すると空のOptionalを返す() {
    IncomeIdentifier unknownId = new IncomeIdentifier(UUID.randomUUID().toString());
    assertTrue(sut.findBy(unknownId).isEmpty());
  }

  @Test
  void 属性で収入の存在を確認できる() {
    IncomeAttribute attribute = registerAttribute("賞与");
    Income income1 = createIncome("夏季賞与", 500000, "2026-06-15", attribute);
    Income income2 = createIncome("冬季賞与", 600000, "2026-12-15", attribute);
    sut.register(income1);
    sut.register(income2);

    assertTrue(sut.existsByAttributeIdentifier(attribute.incomeAttributeIdentifier()));
    assertFalse(
        sut.existsByAttributeIdentifier(
            new IncomeAttributeIdentifier(UUID.randomUUID().toString())));
  }

  @Test
  void 収入を更新できる() {
    IncomeAttribute attribute = registerAttribute("給与2");
    Income income = createIncome("4月給与", 300000, "2026-04-25", attribute);
    sut.register(income);

    Income fetched = sut.findBy(income.incomeIdentifier()).orElseThrow();
    Income updated =
        new Income(
            fetched.incomeIdentifier(),
            new Description("4月給与（修正）"),
            new Amount(350000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute.incomeAttributeIdentifier(),
            fetched.version());
    sut.update(updated);

    Income result = sut.findBy(income.incomeIdentifier()).orElseThrow();
    assertEquals("4月給与（修正）", result.description().value());
    assertEquals(350000, result.amount().value());
  }

  @Test
  void バージョン不一致で更新すると楽観ロック例外が発生する() {
    IncomeAttribute attribute = registerAttribute("給与3");
    Income income = createIncome("4月給与", 300000, "2026-04-25", attribute);
    sut.register(income);

    Income stale =
        new Income(
            income.incomeIdentifier(),
            new Description("更新"),
            new Amount(310000),
            new ReceiveDate(LocalDate.of(2026, 4, 25)),
            attribute.incomeAttributeIdentifier(),
            999L);
    assertThrows(ConcurrentUpdateException.class, () -> sut.update(stale));
  }

  @Test
  void 収入を削除できる() {
    IncomeAttribute attribute = registerAttribute("給与4");
    Income income = createIncome("4月給与", 300000, "2026-04-25", attribute);
    sut.register(income);

    sut.delete(income);
    assertTrue(sut.findBy(income.incomeIdentifier()).isEmpty());
  }

  private IncomeAttribute registerAttribute(String name) {
    IncomeAttribute attribute = IncomeAttribute.create(new IncomeAttributeName(name));
    incomeAttributeDataSource.register(attribute);
    return attribute;
  }

  private Income createIncome(
      String description, int amount, String receiveDate, IncomeAttribute attribute) {
    return new Income(
        new IncomeIdentifier(UUID.randomUUID().toString()),
        new Description(description),
        new Amount(amount),
        new ReceiveDate(LocalDate.parse(receiveDate)),
        attribute.incomeAttributeIdentifier(),
        1L);
  }
}
