package dev.yhiguchi.home_expense.presentation.api;

import dev.yhiguchi.home_expense.query.Pagination;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;

public interface LinkHeaderCreatable {

  default String create(UriInfo uriInfo, Pagination pagination, int totalCount) {
    ArrayList<String> linkList = new ArrayList<>();
    linkList.add(
        String.format(
            "<%s>; rel=\"current\"",
            uriInfo
                .getAbsolutePathBuilder()
                .queryParam("page", pagination.currentPage())
                .queryParam("per_page", pagination.perPage())
                .build()));
    linkList.add(
        String.format(
            "<%s>; rel=\"last\"",
            uriInfo
                .getAbsolutePathBuilder()
                .queryParam("page", pagination.lastPage(totalCount))
                .queryParam("per_page", pagination.perPage())
                .build()));
    linkList.add(
        String.format(
            "<%s>; rel=\"first\"",
            uriInfo
                .getAbsolutePathBuilder()
                .queryParam("page", 1)
                .queryParam("per_page", pagination.perPage())
                .build()));
    if (!pagination.isFirstPage()) {
      linkList.add(
          String.format(
              "<%s>; rel=\"previous\"",
              uriInfo
                  .getAbsolutePathBuilder()
                  .queryParam("page", pagination.previousPage())
                  .queryParam("per_page", pagination.perPage())
                  .build()));
    }
    if (!pagination.isLastPage(totalCount)) {
      linkList.add(
          String.format(
              "<%s>; rel=\"next\"",
              uriInfo
                  .getAbsolutePathBuilder()
                  .queryParam("page", pagination.nextPage())
                  .queryParam("per_page", pagination.perPage())
                  .build()));
    }
    return String.join(", ", linkList);
  }
}
