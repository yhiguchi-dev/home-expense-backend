package dev.yhiguchi.home_expense.query.income;

import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeNotFoundException;
import java.util.Optional;

public interface IncomeSearchResultQuerier {
  IncomeSearchResult search(IncomeSearchCriteria criteria);

  Optional<IncomeDetail> find(IncomeIdentifier id);

  default IncomeDetail get(IncomeIdentifier id) {
    return find(id).orElseThrow(IncomeNotFoundException::new);
  }
}
