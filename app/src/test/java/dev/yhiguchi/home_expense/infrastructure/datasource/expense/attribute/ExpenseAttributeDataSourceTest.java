package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ExpenseAttributeDataSourceTest {

  @Inject ExpenseAttributeDataSource sut;

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
    }
  }

  @Test
  void 登録した経費属性を取得できる() {
    ExpenseAttribute attribute = createAttribute("テスト食費", ExpenseCategory.変動費);
    sut.register(attribute);

    ExpenseAttribute result = sut.get(attribute.expenseAttributeIdentifier());
    assertEquals(attribute.expenseAttributeIdentifier(), result.expenseAttributeIdentifier());
    assertEquals(attribute.expenseAttributeName(), result.expenseAttributeName());
    assertEquals(attribute.expenseCategory(), result.expenseCategory());
  }

  @Test
  void 存在しないIDで取得すると例外が発生する() {
    ExpenseAttributeIdentifier unknownId =
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString());
    assertThrows(ExpenseAttributeNotFoundException.class, () -> sut.get(unknownId));
  }

  @Test
  void 名前で存在確認できる() {
    ExpenseAttribute attribute = createAttribute("家賃", ExpenseCategory.固定費);
    sut.register(attribute);

    assertTrue(sut.existsByName(new ExpenseAttributeName("家賃")));
  }

  @Test
  void 存在しない名前の場合falseを返す() {
    assertFalse(sut.existsByName(new ExpenseAttributeName("存在しない")));
  }

  @Test
  void 経費属性を更新できる() {
    ExpenseAttribute attribute = createAttribute("光熱費", ExpenseCategory.固定費);
    sut.register(attribute);

    ExpenseAttribute fetched = sut.get(attribute.expenseAttributeIdentifier());
    ExpenseAttribute updated =
        new ExpenseAttribute(
            fetched.expenseAttributeIdentifier(),
            new ExpenseAttributeName("水道光熱費"),
            ExpenseCategory.変動費,
            fetched.version());
    sut.update(updated);

    ExpenseAttribute result = sut.get(attribute.expenseAttributeIdentifier());
    assertEquals("水道光熱費", result.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, result.expenseCategory());
  }

  @Test
  void バージョン不一致で更新すると楽観ロック例外が発生する() {
    ExpenseAttribute attribute = createAttribute("通信費", ExpenseCategory.固定費);
    sut.register(attribute);

    ExpenseAttribute staleVersion =
        new ExpenseAttribute(
            attribute.expenseAttributeIdentifier(),
            new ExpenseAttributeName("通信費更新"),
            ExpenseCategory.固定費,
            999L);
    assertThrows(ConcurrentUpdateException.class, () -> sut.update(staleVersion));
  }

  @Test
  void 経費属性を削除できる() {
    ExpenseAttribute attribute = createAttribute("交際費", ExpenseCategory.変動費);
    sut.register(attribute);

    sut.delete(attribute);
    assertThrows(
        ExpenseAttributeNotFoundException.class,
        () -> sut.get(attribute.expenseAttributeIdentifier()));
  }

  private ExpenseAttribute createAttribute(String name, ExpenseCategory category) {
    return new ExpenseAttribute(
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString()),
        new ExpenseAttributeName(name),
        category);
  }
}
