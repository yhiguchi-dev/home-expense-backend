package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.Description;
import dev.yhiguchi.home_expense.domain.model.Amount;
import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute.ExpenseAttributeDataSource;
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
class ExpenseDataSourceTest {

  private static final long INITIAL_VERSION = 1L;

  @Inject ExpenseDataSource sut;

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
  void 経費を登録して取得できる() {
    ExpenseAttribute attribute = registerAttribute("家賃", ExpenseCategory.固定費);
    Expense expense = createExpense("4月家賃", 80000, "2026-04-01", attribute);
    sut.register(expense);

    Expense result = sut.find(expense.expenseIdentifier()).orElseThrow();
    assertEquals(expense.expenseIdentifier(), result.expenseIdentifier());
    assertEquals("4月家賃", result.description().value());
    assertEquals(80000, result.amount().value());
    assertEquals(attribute.expenseAttributeIdentifier(), result.expenseAttributeIdentifier());
  }

  @Test
  void 存在しないIDで取得すると空のOptionalを返す() {
    ExpenseIdentifier unknownId = new ExpenseIdentifier(UUID.randomUUID().toString());
    assertTrue(sut.find(unknownId).isEmpty());
  }

  @Test
  void 属性で経費の存在を確認できる() {
    ExpenseAttribute attribute = registerAttribute("交通費", ExpenseCategory.変動費);
    Expense expense1 = createExpense("電車", 500, "2026-04-01", attribute);
    Expense expense2 = createExpense("バス", 300, "2026-04-02", attribute);
    sut.register(expense1);
    sut.register(expense2);

    assertTrue(sut.existsByAttributeIdentifier(attribute.expenseAttributeIdentifier()));
    assertFalse(
        sut.existsByAttributeIdentifier(
            new ExpenseAttributeIdentifier(UUID.randomUUID().toString())));
  }

  @Test
  void 経費を更新できる() {
    ExpenseAttribute attribute = registerAttribute("食費3", ExpenseCategory.変動費);
    Expense expense = createExpense("ランチ", 1000, "2026-04-10", attribute);
    sut.register(expense);

    Expense fetched = sut.find(expense.expenseIdentifier()).orElseThrow();
    Expense updated =
        new Expense(
            fetched.expenseIdentifier(),
            new Description("ディナー"),
            new Amount(3000),
            new PaymentDate(LocalDate.of(2026, 4, 10)),
            attribute.expenseAttributeIdentifier());
    sut.update(updated, INITIAL_VERSION);

    Expense result = sut.find(expense.expenseIdentifier()).orElseThrow();
    assertEquals("ディナー", result.description().value());
    assertEquals(3000, result.amount().value());
  }

  @Test
  void 更新時に紐付く属性を変更できる() {
    ExpenseAttribute fixedAttr = registerAttribute("保険料", ExpenseCategory.固定費);
    ExpenseAttribute varAttr = registerAttribute("雑費", ExpenseCategory.変動費);
    Expense expense = createExpense("保険", 5000, "2026-04-01", fixedAttr);
    sut.register(expense);

    Expense fetched = sut.find(expense.expenseIdentifier()).orElseThrow();
    Expense updated =
        new Expense(
            fetched.expenseIdentifier(),
            fetched.description(),
            fetched.amount(),
            new PaymentDate(LocalDate.of(2026, 4, 1)),
            varAttr.expenseAttributeIdentifier());
    sut.update(updated, INITIAL_VERSION);

    Expense result = sut.find(expense.expenseIdentifier()).orElseThrow();
    assertEquals(varAttr.expenseAttributeIdentifier(), result.expenseAttributeIdentifier());
  }

  @Test
  void バージョン不一致で更新すると楽観ロック例外が発生する() {
    ExpenseAttribute attribute = registerAttribute("食費4", ExpenseCategory.変動費);
    Expense expense = createExpense("朝食", 500, "2026-04-10", attribute);
    sut.register(expense);

    Expense modified =
        new Expense(
            expense.expenseIdentifier(),
            new Description("朝食更新"),
            new Amount(600),
            new PaymentDate(LocalDate.of(2026, 4, 10)),
            attribute.expenseAttributeIdentifier());
    assertThrows(ConcurrentUpdateException.class, () -> sut.update(modified, 999L));
  }

  @Test
  void 経費を削除できる() {
    ExpenseAttribute attribute = registerAttribute("食費5", ExpenseCategory.変動費);
    Expense expense = createExpense("おやつ", 200, "2026-04-10", attribute);
    sut.register(expense);

    sut.delete(expense);
    assertTrue(sut.find(expense.expenseIdentifier()).isEmpty());
  }

  private ExpenseAttribute registerAttribute(String name, ExpenseCategory category) {
    ExpenseAttribute attribute = ExpenseAttribute.create(new ExpenseAttributeName(name), category);
    expenseAttributeDataSource.register(attribute);
    return attribute;
  }

  private Expense createExpense(
      String description, int price, String paymentDate, ExpenseAttribute attribute) {
    return new Expense(
        new ExpenseIdentifier(UUID.randomUUID().toString()),
        new Description(description),
        new Amount(price),
        new PaymentDate(LocalDate.parse(paymentDate)),
        attribute.expenseAttributeIdentifier());
  }
}
