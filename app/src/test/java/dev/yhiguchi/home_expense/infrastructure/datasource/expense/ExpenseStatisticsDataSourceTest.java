package dev.yhiguchi.home_expense.infrastructure.datasource.expense;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.*;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.domain.model.income.Amount;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.ReceiveDate;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute.ExpenseAttributeDataSource;
import dev.yhiguchi.home_expense.infrastructure.datasource.income.IncomeDataSource;
import dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute.IncomeAttributeDataSource;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatistics;
import dev.yhiguchi.home_expense.query.expense.ExpenseStatisticsCriteria;
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
class ExpenseStatisticsDataSourceTest {

  @Inject ExpenseStatisticsDataSource sut;

  @Inject ExpenseDataSource expenseDataSource;

  @Inject ExpenseAttributeDataSource expenseAttributeDataSource;

  @Inject IncomeDataSource incomeDataSource;

  @Inject IncomeAttributeDataSource incomeAttributeDataSource;

  @Inject DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.fixed_expense")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.variable_expense")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.expense")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.attribute")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.income")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expense.income_attribute")) {
        ps.executeUpdate();
      }
    }
  }

  @Test
  void 月次統計を取得できる() {
    ExpenseAttribute fixedAttr = registerExpenseAttribute("家賃", ExpenseCategory.固定費);
    ExpenseAttribute varAttr = registerExpenseAttribute("食費", ExpenseCategory.変動費);
    registerExpense("4月家賃", 80000, "2026-04-01", fixedAttr);
    registerExpense("ランチ", 1000, "2026-04-10", varAttr);
    registerExpense("ディナー", 3000, "2026-04-10", varAttr);

    IncomeAttribute incomeAttr = registerIncomeAttribute("給与");
    registerIncome("4月給与", 300000, "2026-04-25", incomeAttr);

    ExpenseStatisticsCriteria criteria = new ExpenseStatisticsCriteria(2026, 4);
    ExpenseStatistics result = sut.find(criteria);

    assertEquals(300000L, result.incomeTotalAmount());
    assertEquals(80000L, result.fixedDetail().totalAmount());
    assertEquals(4000L, result.variableDetail().totalAmount());
    assertEquals(1, result.fixedDetail().list().size());
    assertEquals(1, result.variableDetail().list().size());
  }

  @Test
  void 対象期間にデータがない場合はゼロが返る() {
    ExpenseStatisticsCriteria criteria = new ExpenseStatisticsCriteria(2025, 1);
    ExpenseStatistics result = sut.find(criteria);

    assertEquals(0L, result.incomeTotalAmount());
    assertTrue(result.fixedDetail().list().isEmpty());
    assertTrue(result.variableDetail().list().isEmpty());
  }

  @Test
  void 対象月以外のデータは集計に含まれない() {
    ExpenseAttribute attr = registerExpenseAttribute("食費2", ExpenseCategory.変動費);
    registerExpense("4月ランチ", 1000, "2026-04-10", attr);
    registerExpense("5月ランチ", 1200, "2026-05-10", attr);

    ExpenseStatisticsCriteria criteria = new ExpenseStatisticsCriteria(2026, 4);
    ExpenseStatistics result = sut.find(criteria);

    assertEquals(1000L, result.variableDetail().totalAmount());
  }

  private ExpenseAttribute registerExpenseAttribute(String name, ExpenseCategory category) {
    ExpenseAttribute attribute =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier(UUID.randomUUID().toString()),
            new ExpenseAttributeName(name),
            category);
    expenseAttributeDataSource.register(attribute);
    return attribute;
  }

  private void registerExpense(
      String description, int price, String paymentDate, ExpenseAttribute attribute) {
    Expense expense =
        new Expense(
            new ExpenseIdentifier(UUID.randomUUID().toString()),
            new Description(description),
            new Price(price),
            new PaymentDate(LocalDate.parse(paymentDate)),
            attribute,
            1L);
    expenseDataSource.register(expense);
  }

  private IncomeAttribute registerIncomeAttribute(String name) {
    IncomeAttribute attribute =
        new IncomeAttribute(
            new IncomeAttributeIdentifier(UUID.randomUUID().toString()),
            new IncomeAttributeName(name));
    incomeAttributeDataSource.register(attribute);
    return attribute;
  }

  private void registerIncome(
      String description, int amount, String receiveDate, IncomeAttribute attribute) {
    Income income =
        new Income(
            new IncomeIdentifier(UUID.randomUUID().toString()),
            new dev.yhiguchi.home_expense.domain.model.income.Description(description),
            new Amount(amount),
            new ReceiveDate(LocalDate.parse(receiveDate)),
            attribute,
            1L);
    incomeDataSource.register(income);
  }
}
