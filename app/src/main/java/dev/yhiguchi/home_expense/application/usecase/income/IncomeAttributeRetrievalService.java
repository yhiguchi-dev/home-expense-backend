package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttribute;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.attribute.IncomeAttributeRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncomeAttributeRetrievalService {

  IncomeAttributeRepository incomeAttributeRepository;

  public IncomeAttributeRetrievalService(IncomeAttributeRepository incomeAttributeRepository) {
    this.incomeAttributeRepository = incomeAttributeRepository;
  }

  public Revision<IncomeAttribute> get(IncomeAttributeIdentifier incomeAttributeIdentifier) {
    return incomeAttributeRepository
        .findBy(incomeAttributeIdentifier)
        .orElseThrow(IncomeAttributeNotFoundException::new);
  }
}
