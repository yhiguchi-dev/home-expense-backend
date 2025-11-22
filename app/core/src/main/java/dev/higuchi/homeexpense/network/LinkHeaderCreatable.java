package dev.higuchi.homeexpense.network;

import dev.higuchi.homeexpense.query.model.pagination.Pagination;
import java.net.URI;
import java.util.ArrayList;

public interface LinkHeaderCreatable {

  default String create(URI uri, Pagination pagination, int totalCount) {
    ArrayList<String> linkList = new ArrayList<>();
    linkList.add(
        String.format(
            "<%s>; rel=\"current\"",
            uri.resolve(
                String.format(
                    "?page=%d&per_page=%d", pagination.currentPage(), pagination.perPage()))));
    linkList.add(
        String.format(
            "<%s>; rel=\"last\"",
            uri.resolve(
                String.format(
                    "?page=%d&per_page=%d",
                    pagination.lastPage(totalCount), pagination.perPage()))));
    linkList.add(
        String.format(
            "<%s>; rel=\"first\"",
            uri.resolve(String.format("?page=%d&per_page=%d", 1, pagination.perPage()))));
    if (!pagination.isFirstPage()) {
      linkList.add(
          String.format(
              "<%s>; rel=\"previous\"",
              uri.resolve(
                  String.format(
                      "?page=%d&per_page=%d", pagination.previousPage(), pagination.perPage()))));
    }
    if (!pagination.isLastPage(totalCount)) {
      linkList.add(
          String.format(
              "<%s>; rel=\"next\"",
              uri.resolve(
                  String.format(
                      "?page=%d&per_page=%d", pagination.nextPage(), pagination.perPage()))));
    }
    return String.join(", ", linkList);
  }
}
