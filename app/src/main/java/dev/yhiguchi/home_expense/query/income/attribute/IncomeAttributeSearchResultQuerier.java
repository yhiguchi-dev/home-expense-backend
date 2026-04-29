package dev.yhiguchi.home_expense.query.income.attribute;

import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import java.util.Optional;

public interface IncomeAttributeSearchResultQuerier {
  IncomeAttributeSearchResult search(IncomeAttributeSearchCriteria criteria);

  Optional<IncomeAttributeDetail> find(IncomeAttributeIdentifier id);

  default IncomeAttributeDetail get(IncomeAttributeIdentifier id) {
    return find(id).orElseThrow(IncomeAttributeNotFoundException::new);
  }
}
