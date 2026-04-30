package dev.yhiguchi.home_expense.query;

public record Pagination(int page, int perPage) {

  public static final int PAGE_MIN = 1;
  public static final int PER_PAGE_MIN = 1;
  public static final int PER_PAGE_MAX = 100;

  public Pagination {
    if (page < PAGE_MIN) {
      throw new IllegalArgumentException("page must be >= " + PAGE_MIN);
    }
    if (perPage < PER_PAGE_MIN || perPage > PER_PAGE_MAX) {
      throw new IllegalArgumentException(
          "perPage must be between " + PER_PAGE_MIN + " and " + PER_PAGE_MAX);
    }
  }

  public int offset() {
    return (page - 1) * perPage;
  }

  public int previousPage() {
    return page - 1;
  }

  public int nextPage() {
    return page + 1;
  }

  public int lastPage(int totalCount) {
    int division = totalCount / perPage;
    return totalCount % perPage > 0 ? division + 1 : division;
  }

  public boolean isFirstPage() {
    return page == PAGE_MIN;
  }

  public boolean isLastPage(int totalCount) {
    return page == lastPage(totalCount);
  }
}
