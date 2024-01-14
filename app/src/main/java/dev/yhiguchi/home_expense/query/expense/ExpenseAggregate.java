package dev.yhiguchi.home_expense.query.expense;

public class ExpenseAggregate {

  Integer incomeTotalAmount;

  ExpenseAggregateDetail fixedDetail;

  ExpenseAggregateDetail variableDetail;

  public ExpenseAggregate(
      Integer incomeTotalAmount,
      ExpenseAggregateDetail fixedDetail,
      ExpenseAggregateDetail variableDetail) {
    this.incomeTotalAmount = incomeTotalAmount;
    this.fixedDetail = fixedDetail;
    this.variableDetail = variableDetail;
  }

  ExpenseAggregate() {
    this(0, new ExpenseAggregateDetail(), new ExpenseAggregateDetail());
  }

  public Integer incomeTotalAmount() {
    return incomeTotalAmount;
  }

  public Integer disposableIncome() {
    return incomeTotalAmount - totalAmount();
  }

  public Integer totalAmount() {
    return fixedDetail().totalAmount() + variableDetail().totalAmount();
  }

  public ExpenseAggregateDetail fixedDetail() {
    return fixedDetail;
  }

  public ExpenseAggregateDetail variableDetail() {
    return variableDetail;
  }
}
