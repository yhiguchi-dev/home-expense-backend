package dev.yhiguchi.home_expense.query;

public class Pagination {
  Page page;
  PerPage perPage;

  public Pagination(Page page, PerPage perPage) {
    this.page = page;
    this.perPage = perPage;
  }

  public int offset() {
    return (page.value() - 1) * perPage.value();
  }

  public int page() {
    return page.value();
  }

  public int perPage() {
    return perPage.value();
  }
}
