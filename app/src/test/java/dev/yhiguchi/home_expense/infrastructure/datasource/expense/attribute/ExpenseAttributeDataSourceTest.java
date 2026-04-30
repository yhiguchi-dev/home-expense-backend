package dev.yhiguchi.home_expense.infrastructure.datasource.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.*;
import dev.yhiguchi.home_expense.infrastructure.datasource.ConcurrentUpdateException;
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

  private static final long INITIAL_VERSION = 1L;

  @Inject ExpenseAttributeDataSource sut;

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
  void 登録した経費属性を取得できる() {
    ExpenseAttribute attribute = createAttribute("テスト食費", ExpenseCategory.変動費);
    sut.register(attribute);

    ExpenseAttribute result = sut.find(attribute.expenseAttributeIdentifier()).orElseThrow();
    assertEquals(attribute.expenseAttributeIdentifier(), result.expenseAttributeIdentifier());
    assertEquals(attribute.expenseAttributeName(), result.expenseAttributeName());
    assertEquals(attribute.expenseCategory(), result.expenseCategory());
  }

  @Test
  void 存在しないIDで取得すると空のOptionalを返す() {
    ExpenseAttributeIdentifier unknownId =
        new ExpenseAttributeIdentifier(UUID.randomUUID().toString());
    assertTrue(sut.find(unknownId).isEmpty());
  }

  @Test
  void 名前と分類の組合せで存在確認できる() {
    ExpenseAttribute attribute = createAttribute("家賃", ExpenseCategory.固定費);
    sut.register(attribute);

    assertTrue(sut.existsByName(new ExpenseAttributeName("家賃"), ExpenseCategory.固定費));
  }

  @Test
  void 存在しない名前の場合falseを返す() {
    assertFalse(sut.existsByName(new ExpenseAttributeName("存在しない"), ExpenseCategory.固定費));
  }

  @Test
  void 同名でも分類が異なる場合はfalseを返す() {
    ExpenseAttribute attribute = createAttribute("家賃", ExpenseCategory.固定費);
    sut.register(attribute);

    assertFalse(sut.existsByName(new ExpenseAttributeName("家賃"), ExpenseCategory.変動費));
  }

  @Test
  void 経費属性を更新できる() {
    ExpenseAttribute attribute = createAttribute("光熱費", ExpenseCategory.固定費);
    sut.register(attribute);

    ExpenseAttribute fetched = sut.find(attribute.expenseAttributeIdentifier()).orElseThrow();
    ExpenseAttribute updated =
        new ExpenseAttribute(
            fetched.expenseAttributeIdentifier(),
            new ExpenseAttributeName("水道光熱費"),
            ExpenseCategory.変動費);
    sut.update(updated, INITIAL_VERSION);

    ExpenseAttribute result = sut.find(attribute.expenseAttributeIdentifier()).orElseThrow();
    assertEquals("水道光熱費", result.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, result.expenseCategory());
  }

  @Test
  void バージョン不一致で更新すると楽観ロック例外が発生する() {
    ExpenseAttribute attribute = createAttribute("通信費", ExpenseCategory.固定費);
    sut.register(attribute);

    ExpenseAttribute modified =
        new ExpenseAttribute(
            attribute.expenseAttributeIdentifier(),
            new ExpenseAttributeName("通信費更新"),
            ExpenseCategory.固定費);
    assertThrows(ConcurrentUpdateException.class, () -> sut.update(modified, 999L));
  }

  @Test
  void 経費属性を削除できる() {
    ExpenseAttribute attribute = createAttribute("交際費", ExpenseCategory.変動費);
    sut.register(attribute);

    sut.delete(attribute);
    assertTrue(sut.find(attribute.expenseAttributeIdentifier()).isEmpty());
  }

  private ExpenseAttribute createAttribute(String name, ExpenseCategory category) {
    return ExpenseAttribute.create(new ExpenseAttributeName(name), category);
  }
}
