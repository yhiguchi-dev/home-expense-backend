package dev.yhiguchi.home_expense.query.expense.attribute;

import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.expense.attribute.ExpenseAttributeNotFoundException;
import java.util.Optional;

public interface ExpenseAttributeSearchResultQuerier {
  ExpenseAttributeSearchResult search(ExpenseAttributeSearchCriteria criteria);

  Optional<ExpenseAttributeDetail> find(ExpenseAttributeIdentifier id);

  default ExpenseAttributeDetail get(ExpenseAttributeIdentifier id) {
    return find(id).orElseThrow(ExpenseAttributeNotFoundException::new);
  }
}
