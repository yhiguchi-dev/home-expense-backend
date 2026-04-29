package dev.yhiguchi.home_expense.query.expense;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseStatisticsDetailTest {

  @Test
  void リストからtotalAmountを集計できる() {
    var list =
        List.of(
            new ExpenseAttributeStatistics("00000000-0000-0000-0000-000000000001", "食費", 50000L),
            new ExpenseAttributeStatistics("00000000-0000-0000-0000-000000000002", "日用品", 30000L));

    var detail = new ExpenseStatisticsDetail(list);

    assertEquals(80000L, detail.totalAmount());
    assertEquals(2, detail.list().size());
  }

  @Test
  void 空リストの場合totalAmountが0になる() {
    var detail = new ExpenseStatisticsDetail(List.of());

    assertEquals(0L, detail.totalAmount());
    assertTrue(detail.list().isEmpty());
  }
}
