package dev.higuchi.homeexpense.springboot.presentation.api.income;

import dev.higuchi.homeexpense.command.model.income.Income;
import dev.higuchi.homeexpense.command.model.income.IncomeIdentifier;
import dev.higuchi.homeexpense.network.LinkHeaderCreatable;
import dev.higuchi.homeexpense.query.model.income.IncomeSummary;
import dev.higuchi.homeexpense.query.model.income.IncomeSummaryCriteria;
import dev.higuchi.homeexpense.query.model.pagination.Page;
import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import dev.higuchi.homeexpense.query.model.pagination.PerPage;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeDeletionService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeGettingService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeRegistrationService;
import dev.higuchi.homeexpense.springboot.application.usecase.income.IncomeUpdateService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    path = "/v1/incomes",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class IncomeApi implements LinkHeaderCreatable {

  IncomeRegistrationService incomeRegistrationService;
  IncomeUpdateService incomeUpdateService;
  IncomeDeletionService incomeDeletionService;
  IncomeGettingService incomeGettingService;

  public IncomeApi(
      IncomeRegistrationService incomeRegistrationService,
      IncomeUpdateService incomeUpdateService,
      IncomeDeletionService incomeDeletionService,
      IncomeGettingService incomeGettingService) {
    this.incomeRegistrationService = incomeRegistrationService;
    this.incomeUpdateService = incomeUpdateService;
    this.incomeDeletionService = incomeDeletionService;
    this.incomeGettingService = incomeGettingService;
  }

  @PostMapping
  public ResponseEntity<?> post(@Valid IncomePostRequest request) {
    IncomeIdentifier incomeIdentifier =
        incomeRegistrationService.createAndRegister(
            request.toDescription(),
            request.toAmount(),
            request.toReceiveDate(),
            request.toIncomeAttributeIdentifier());
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path(incomeIdentifier.value())
            .build()
            .toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<?> put(@PathVariable("id") String id, @Valid IncomePutRequest request) {
    incomeUpdateService.update(
        new IncomeIdentifier(id),
        request.toDescription(),
        request.toAmount(),
        request.toReceiveDate(),
        request.toIncomeAttributeIdentifier());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable("id") String id) {
    incomeDeletionService.delete(new IncomeIdentifier(id));
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<?> get(
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "per_page", defaultValue = "20") Integer perPage,
      @RequestParam("year") Integer year) {
    Pagination pagination = new Pagination(new Page(page), new PerPage(perPage));
    IncomeSummaryCriteria criteria = new IncomeSummaryCriteria(pagination, year);
    IncomeSummary incomeSummary = incomeGettingService.findSummary(criteria);
    IncomeGetListResponse response =
        page <= incomeSummary.totalCount()
            ? new IncomeGetListResponse(incomeSummary)
            : new IncomeGetListResponse();
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
    String links = create(uri, criteria.pagination(), incomeSummary.totalCount());
    return ResponseEntity.ok().header("Link", links).body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<?> get(@PathVariable("id") String id) {
    Income income = incomeGettingService.get(new IncomeIdentifier(id));
    IncomeGetResponse response = IncomeGetResponse.from(income);
    return ResponseEntity.ok(response);
  }
}
