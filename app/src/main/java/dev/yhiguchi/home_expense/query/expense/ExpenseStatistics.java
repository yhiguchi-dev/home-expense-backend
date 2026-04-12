package dev.yhiguchi.home_expense.query.expense;

public class ExpenseStatistics {

  long incomeTotalAmount;

  ExpenseStatisticsDetail fixedDetail;

  ExpenseStatisticsDetail variableDetail;

  public ExpenseStatistics(
      long incomeTotalAmount,
      ExpenseStatisticsDetail fixedDetail,
      ExpenseStatisticsDetail variableDetail) {
    this.incomeTotalAmount = incomeTotalAmount;
    this.fixedDetail = fixedDetail;
    this.variableDetail = variableDetail;
  }

  ExpenseStatistics() {
    this(0L, new ExpenseStatisticsDetail(), new ExpenseStatisticsDetail());
  }

  public long incomeTotalAmount() {
    return incomeTotalAmount;
  }

  public long disposableIncome() {
    return Math.subtractExact(incomeTotalAmount, totalAmount());
  }

  public long totalAmount() {
    return Math.addExact(fixedDetail().totalAmount(), variableDetail().totalAmount());
  }

  public ExpenseStatisticsDetail fixedDetail() {
    return fixedDetail;
  }

  public ExpenseStatisticsDetail variableDetail() {
    return variableDetail;
  }
}
