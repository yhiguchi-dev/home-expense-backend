package dev.higuchi.homeexpense.springboot.presentation.api.income.attribute;

import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttribute;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeAlreadyExistsException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeConstraintException;
import dev.higuchi.homeexpense.command.model.income.attribute.IncomeAttributeIdentifier;
import dev.higuchi.homeexpense.network.LinkHeaderCreatable;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummary;
import dev.higuchi.homeexpense.query.model.income.attribute.IncomeAttributeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.pagination.Page;
import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import dev.higuchi.homeexpense.query.model.pagination.PerPage;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeAttributeDeletionService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeAttributeGettingService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeAttributeRegistrationService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeAttributeUpdateService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    path = "/v1/income-attributes",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class IncomeAttributeApi implements LinkHeaderCreatable {

  IncomeAttributeRegistrationService incomeAttributeRegistrationService;
  IncomeAttributeUpdateService incomeAttributeUpdateService;
  IncomeAttributeDeletionService incomeAttributeDeletionService;

  IncomeAttributeGettingService incomeAttributeGettingService;

  public IncomeAttributeApi(
      IncomeAttributeRegistrationService incomeAttributeRegistrationService,
      IncomeAttributeUpdateService incomeAttributeUpdateService,
      IncomeAttributeDeletionService incomeAttributeDeletionService,
      IncomeAttributeGettingService incomeAttributeGettingService) {
    this.incomeAttributeRegistrationService = incomeAttributeRegistrationService;
    this.incomeAttributeUpdateService = incomeAttributeUpdateService;
    this.incomeAttributeDeletionService = incomeAttributeDeletionService;
    this.incomeAttributeGettingService = incomeAttributeGettingService;
  }

  @PostMapping
  public ResponseEntity<?> post(@Valid IncomeAttributePostRequest request) {
    IncomeAttributeIdentifier incomeAttributeIdentifier =
        incomeAttributeRegistrationService.createAndRegister(request.toIncomeAttributeName());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path(incomeAttributeIdentifier.value())
            .build()
            .toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<?> put(
      @PathVariable("id") String id, @Valid IncomeAttributePutRequest request) {
    incomeAttributeUpdateService.update(
        new IncomeAttributeIdentifier(id), request.toIncomeAttributeName());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable("id") String id) {
    incomeAttributeDeletionService.delete(new IncomeAttributeIdentifier(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<?> get(
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "per_page", defaultValue = "20") Integer perPage) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeAttributeSummaryCriteria criteria = new IncomeAttributeSummaryCriteria(pagination);
    IncomeAttributeSummary incomeAttributeSummary =
        incomeAttributeGettingService.findSummary(criteria);
    IncomeAttributeGetSummaryResponse response =
        page <= incomeAttributeSummary.totalCount()
            ? new IncomeAttributeGetSummaryResponse(incomeAttributeSummary)
            : new IncomeAttributeGetSummaryResponse();
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    String links = create(uri, pagination, incomeAttributeSummary.totalCount());
    return ResponseEntity.ok().header("Link", links).body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<?> get(@PathVariable("id") String id) {
    IncomeAttribute incomeAttribute =
        incomeAttributeGettingService.get(new IncomeAttributeIdentifier(id));
    IncomeAttributeGetResponse response = IncomeAttributeGetResponse.from(incomeAttribute);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler
  public ResponseEntity<String> handleException(IncomeAttributeAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("既に登録されています");
  }

  @ExceptionHandler
  public ResponseEntity<String> mapException(IncomeAttributeConstraintException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("制約があるため削除できません");
  }
}
