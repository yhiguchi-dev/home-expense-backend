package dev.yhiguchi.home_expense.query.expense;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseStatisticsTest {

  @Test
  void totalAmountで固定費と変動費の合計を計算できる() {
    var statistics =
        new ExpenseStatistics(
            500000L,
            new ExpenseStatisticsDetail(100000L, List.of()),
            new ExpenseStatisticsDetail(200000L, List.of()));

    assertEquals(300000L, statistics.totalAmount());
  }

  @Test
  void disposableIncomeで可処分所得を計算できる() {
    var statistics =
        new ExpenseStatistics(
            500000L,
            new ExpenseStatisticsDetail(100000L, List.of()),
            new ExpenseStatisticsDetail(200000L, List.of()));

    assertEquals(200000L, statistics.disposableIncome());
  }

  @Test
  void totalAmountでオーバーフロー時に例外が発生する() {
    var statistics =
        new ExpenseStatistics(
            0L,
            new ExpenseStatisticsDetail(Long.MAX_VALUE, List.of()),
            new ExpenseStatisticsDetail(1L, List.of()));

    assertThrows(ArithmeticException.class, statistics::totalAmount);
  }

  @Test
  void disposableIncomeでアンダーフロー時に例外が発生する() {
    var statistics =
        new ExpenseStatistics(
            Long.MIN_VALUE,
            new ExpenseStatisticsDetail(1L, List.of()),
            new ExpenseStatisticsDetail(0L, List.of()));

    assertThrows(ArithmeticException.class, statistics::disposableIncome);
  }
}
