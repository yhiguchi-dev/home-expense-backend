package dev.higuchi.homeexpense.springboot.presentation.api.expense;

import dev.higuchi.homeexpense.command.model.expense.Expense;
import dev.higuchi.homeexpense.command.model.expense.ExpenseIdentifier;
import dev.higuchi.homeexpense.network.http.LinkHeaderCreatable;
import dev.higuchi.homeexpense.query.model.expense.ExpenseCriteriaCreator;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummary;
import dev.higuchi.homeexpense.query.model.expense.ExpenseSummaryCriteria;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseDeletionService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseGettingService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseRegistrationService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseUpdateService;
import dev.higuchi.homeexpense.springboot.presentation.validation.ExpenseCategory;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    path = "/v1/expenses",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseApi implements LinkHeaderCreatable {

  ExpenseRegistrationService expenseRegistrationService;
  ExpenseGettingService expenseGettingService;
  ExpenseUpdateService expenseUpdateService;
  ExpenseDeletionService expenseDeletionService;

  public ExpenseApi(
      ExpenseRegistrationService expenseRegistrationService,
      ExpenseGettingService expenseGettingService,
      ExpenseUpdateService expenseUpdateService,
      ExpenseDeletionService expenseDeletionService) {
    this.expenseRegistrationService = expenseRegistrationService;
    this.expenseGettingService = expenseGettingService;
    this.expenseUpdateService = expenseUpdateService;
    this.expenseDeletionService = expenseDeletionService;
  }

  @PostMapping
  public ResponseEntity<?> post(@Valid ExpensePostRequest request) {
    ExpenseIdentifier expenseIdentifier =
        expenseRegistrationService.createAndRegister(
            request.toDescription(),
            request.toPrice(),
            request.toPaymentDate(),
            request.toExpenseAttributeIdentifier());
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<?> put(@PathVariable("id") String id, @Valid ExpensePutRequest request) {
    expenseUpdateService.update(
        new ExpenseIdentifier(id),
        request.toDescription(),
        request.toPrice(),
        request.toPaymentDate(),
        request.toExpenseAttributeIdentifier());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable("id") String id) {
    expenseDeletionService.delete(new ExpenseIdentifier(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<?> get(
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "per_page", defaultValue = "20") Integer perPage,
      @RequestParam("year") Integer year,
      @RequestParam("month") Integer month,
      @RequestParam("category") @ExpenseCategory String category,
      @RequestParam("attribute_id") String attributeId) {
    ExpenseSummaryCriteria criteria =
        ExpenseCriteriaCreator.create(page, perPage, year, month, category, attributeId);
    ExpenseSummary expenseSummary = expenseGettingService.findSummary(criteria);
    ExpenseGetListResponse response =
        page <= expenseSummary.totalCount()
            ? new ExpenseGetListResponse(expenseSummary)
            : new ExpenseGetListResponse();
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    String links = create(uri, criteria.pagination(), expenseSummary.totalCount());
    return ResponseEntity.ok().header("Link", links).body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<?> get(@PathVariable("id") String id) {
    Expense expense = expenseGettingService.get(new ExpenseIdentifier(id));
    ExpenseGetResponse response = ExpenseGetResponse.from(expense);
    return ResponseEntity.ok(response);
  }
}
