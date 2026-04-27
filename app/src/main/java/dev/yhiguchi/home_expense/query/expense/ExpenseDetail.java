package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.Expense;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttribute;

/** 経費とその属性をまとめた読取用レコード（version は楽観ロック用） */
public record ExpenseDetail(Expense expense, ExpenseAttribute attribute, long version) {}
