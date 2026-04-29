package dev.yhiguchi.home_expense.query.expense;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.ExpenseNotFoundException;
import java.util.Optional;

public interface ExpenseSearchResultQuerier {
  ExpenseSearchResult search(ExpenseSearchCriteria criteria);

  Optional<ExpenseDetail> find(ExpenseIdentifier id);

  default ExpenseDetail get(ExpenseIdentifier id) {
    return find(id).orElseThrow(ExpenseNotFoundException::new);
  }
}
