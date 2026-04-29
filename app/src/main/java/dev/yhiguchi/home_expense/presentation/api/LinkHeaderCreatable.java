package dev.yhiguchi.home_expense.presentation.api;

import dev.yhiguchi.home_expense.query.Pagination;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;

/** RFC 8288 Link ヘッダーの組立ユーティリティ。 */
public final class LinkHeaderCreatable {

  private LinkHeaderCreatable() {}

  public static String create(UriInfo uriInfo, Pagination pagination, int totalCount) {
    ArrayList<String> linkList = new ArrayList<>();
    linkList.add(linkOf(uriInfo, "current", pagination.currentPage(), pagination.perPage()));
    linkList.add(linkOf(uriInfo, "last", pagination.lastPage(totalCount), pagination.perPage()));
    linkList.add(linkOf(uriInfo, "first", 1, pagination.perPage()));
    if (!pagination.isFirstPage()) {
      linkList.add(linkOf(uriInfo, "previous", pagination.previousPage(), pagination.perPage()));
    }
    if (!pagination.isLastPage(totalCount)) {
      linkList.add(linkOf(uriInfo, "next", pagination.nextPage(), pagination.perPage()));
    }
    return String.join(", ", linkList);
  }

  private static String linkOf(UriInfo uriInfo, String rel, int page, int perPage) {
    return String.format(
        "<%s>; rel=\"%s\"",
        uriInfo
            .getAbsolutePathBuilder()
            .queryParam("page", page)
            .queryParam("per_page", perPage)
            .build(),
        rel);
  }
}
