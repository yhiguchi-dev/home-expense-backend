package dev.higuchi.homeexpense.springboot.presentation.api.expense.attribute;

import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeAlreadyExistsException;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeConstraintException;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.network.LinkHeaderCreatable;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummary;
import dev.higuchi.homeexpense.query.model.expense.attribute.ExpenseAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.pagination.Page;
import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import dev.higuchi.homeexpense.query.model.pagination.PerPage;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseAttributeDeletionService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseAttributeGettingService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseAttributeRegistrationService;
import dev.higuchi.homeexpense.springboot.application.usecase.expense.ExpenseAttributeUpdateService;
import dev.higuchi.homeexpense.springboot.presentation.validation.ExpenseCategory;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Objects;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    path = "/v1/expense-attributes",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseAttributeApi implements LinkHeaderCreatable {

  ExpenseAttributeRegistrationService expenseAttributeRegistrationService;
  ExpenseAttributeGettingService expenseAttributeGettingService;
  ExpenseAttributeUpdateService expenseAttributeUpdateService;

  ExpenseAttributeDeletionService expenseAttributeDeletionService;

  public ExpenseAttributeApi(
      ExpenseAttributeRegistrationService expenseAttributeRegistrationService,
      ExpenseAttributeGettingService expenseAttributeGettingService,
      ExpenseAttributeUpdateService expenseAttributeUpdateService,
      ExpenseAttributeDeletionService expenseAttributeDeletionService) {
    this.expenseAttributeRegistrationService = expenseAttributeRegistrationService;
    this.expenseAttributeGettingService = expenseAttributeGettingService;
    this.expenseAttributeUpdateService = expenseAttributeUpdateService;
    this.expenseAttributeDeletionService = expenseAttributeDeletionService;
  }

  @PostMapping
  public ResponseEntity<?> post(@Valid @RequestBody ExpenseAttributePostRequest request) {
    ExpenseAttributeIdentifier expenseAttributeIdentifier =
        expenseAttributeRegistrationService.createAndRegister(
            request.toExpenseAttributeName(), request.toExpenseCategory());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path(expenseAttributeIdentifier.value())
            .build()
            .toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<?> put(
      @PathVariable("id") String id, @Valid ExpenseAttributePutRequest request) {
    expenseAttributeUpdateService.update(
        new ExpenseAttributeIdentifier(id),
        request.toExpenseAttributeName(),
        request.toExpenseCategory());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable("id") String id) {
    expenseAttributeDeletionService.delete(new ExpenseAttributeIdentifier(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<?> get(
      @RequestParam("category") @ExpenseCategory String category,
      @RequestParam("page") @DefaultValue("1") Integer page,
      @RequestParam("per_page") @DefaultValue("20") Integer perPage) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    ExpenseAttributeSummaryCriteria criteria =
        Objects.nonNull(category)
            ? new ExpenseAttributeSummaryCriteria(
                dev.higuchi.homeexpense.command.model.expense.ExpenseCategory.of(category),
                pagination)
            : new ExpenseAttributeSummaryCriteria(pagination);
    ExpenseAttributeSummary expenseAttributeSummary =
        expenseAttributeGettingService.findSummary(criteria);
    ExpenseAttributeGetSummaryResponse response =
        page <= expenseAttributeSummary.totalCount()
            ? new ExpenseAttributeGetSummaryResponse(expenseAttributeSummary)
            : new ExpenseAttributeGetSummaryResponse();
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    String links = create(uri, pagination, expenseAttributeSummary.totalCount());
    return ResponseEntity.ok().header("Link", links).body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<?> get(@PathVariable("id") String id) {
    ExpenseAttribute expenseAttribute =
        expenseAttributeGettingService.get(new ExpenseAttributeIdentifier(id));
    ExpenseAttributeGetResponse response = ExpenseAttributeGetResponse.from(expenseAttribute);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler
  public ResponseEntity<?> handleException(ExpenseAttributeAlreadyExistsException e) {
    return ResponseEntity.badRequest().body("既に登録されています");
  }

  @ExceptionHandler
  public ResponseEntity<?> handleException(ExpenseAttributeConstraintException e) {
    return ResponseEntity.badRequest().body("制約があるため削除できません");
  }
}
