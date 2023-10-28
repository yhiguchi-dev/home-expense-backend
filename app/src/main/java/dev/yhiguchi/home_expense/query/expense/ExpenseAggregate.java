package dev.yhiguchi.home_expense.query.expense;

public class ExpenseAggregate {

  ExpenseAggregateDetail fixedDetail;

  ExpenseAggregateDetail variableDetail;

  public ExpenseAggregate(
      ExpenseAggregateDetail fixedDetail, ExpenseAggregateDetail variableDetail) {
    this.fixedDetail = fixedDetail;
    this.variableDetail = variableDetail;
  }

  ExpenseAggregate() {
    this(new ExpenseAggregateDetail(), new ExpenseAggregateDetail());
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
