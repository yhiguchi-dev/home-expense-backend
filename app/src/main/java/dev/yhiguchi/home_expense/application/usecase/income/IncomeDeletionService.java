package dev.yhiguchi.home_expense.application.usecase.income;

import dev.yhiguchi.home_expense.domain.model.Revision;
import dev.yhiguchi.home_expense.domain.model.income.Income;
import dev.yhiguchi.home_expense.domain.model.income.IncomeIdentifier;
import dev.yhiguchi.home_expense.domain.model.income.IncomeNotFoundException;
import dev.yhiguchi.home_expense.domain.model.income.IncomeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class IncomeDeletionService {

  IncomeRepository incomeRepository;

  public IncomeDeletionService(IncomeRepository incomeRepository) {
    this.incomeRepository = incomeRepository;
  }

  public void delete(IncomeIdentifier incomeIdentifier) {
    Income income =
        incomeRepository
            .findBy(incomeIdentifier)
            .map(Revision::entity)
            .orElseThrow(IncomeNotFoundException::new);
    incomeRepository.delete(income);
  }
}
