package dev.yhiguchi.home_expense.infrastructure.datasource.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.ConcurrentUpdateException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.*;
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
class IncomeAttributeDataSourceTest {

  @Inject IncomeAttributeDataSource sut;

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
  void 登録した収入属性を取得できる() {
    IncomeAttribute attribute = createAttribute("給与");
    sut.register(attribute);

    IncomeAttribute result = sut.get(attribute.incomeAttributeIdentifier());
    assertEquals(attribute.incomeAttributeIdentifier(), result.incomeAttributeIdentifier());
    assertEquals("給与", result.incomeAttributeName().value());
  }

  @Test
  void 存在しないIDで取得すると例外が発生する() {
    IncomeAttributeIdentifier unknownId =
        new IncomeAttributeIdentifier(UUID.randomUUID().toString());
    assertThrows(IncomeAttributeNotFoundException.class, () -> sut.get(unknownId));
  }

  @Test
  void 名前で存在確認できる() {
    IncomeAttribute attribute = createAttribute("賞与");
    sut.register(attribute);

    assertTrue(sut.existsByName(new IncomeAttributeName("賞与")));
  }

  @Test
  void 存在しない名前の場合falseを返す() {
    assertFalse(sut.existsByName(new IncomeAttributeName("存在しない")));
  }

  @Test
  void 収入属性を更新できる() {
    IncomeAttribute attribute = createAttribute("副業");
    sut.register(attribute);

    IncomeAttribute fetched = sut.get(attribute.incomeAttributeIdentifier());
    IncomeAttribute updated =
        new IncomeAttribute(
            fetched.incomeAttributeIdentifier(),
            new IncomeAttributeName("副業収入"),
            fetched.version());
    sut.update(updated);

    IncomeAttribute result = sut.get(attribute.incomeAttributeIdentifier());
    assertEquals("副業収入", result.incomeAttributeName().value());
  }

  @Test
  void バージョン不一致で更新すると楽観ロック例外が発生する() {
    IncomeAttribute attribute = createAttribute("配当");
    sut.register(attribute);

    IncomeAttribute staleVersion =
        new IncomeAttribute(
            attribute.incomeAttributeIdentifier(), new IncomeAttributeName("配当更新"), 999L);
    assertThrows(ConcurrentUpdateException.class, () -> sut.update(staleVersion));
  }

  @Test
  void 収入属性を削除できる() {
    IncomeAttribute attribute = createAttribute("その他");
    sut.register(attribute);

    sut.delete(attribute.incomeAttributeIdentifier());
    assertThrows(
        IncomeAttributeNotFoundException.class,
        () -> sut.get(attribute.incomeAttributeIdentifier()));
  }

  private IncomeAttribute createAttribute(String name) {
    return new IncomeAttribute(
        new IncomeAttributeIdentifier(UUID.randomUUID().toString()), new IncomeAttributeName(name));
  }
}
