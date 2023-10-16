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

  public int currentPage() {
    return page.value();
  }

  public int perPage() {
    return perPage.value();
  }

  public int previousPage() {
    return currentPage() - 1;
  }

  public int nextPage() {
    return currentPage() + 1;
  }

  public int lastPage(int totalCount) {
    int division = totalCount / perPage();
    return totalCount % perPage() > 0 ? division + 1 : division;
  }

  public boolean isFirstPage() {
    return currentPage() == 1;
  }

  public boolean isLastPage(int totalCount) {
    return currentPage() == lastPage(totalCount);
  }
}
