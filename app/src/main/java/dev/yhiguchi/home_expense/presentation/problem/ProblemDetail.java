package dev.yhiguchi.home_expense.presentation.problem;

import java.net.URI;

/** RFC 9457 Problem Details for HTTP APIs */
public record ProblemDetail(URI type, String title, int status, String detail, URI instance) {

  private static final URI ABOUT_BLANK = URI.create("about:blank");

  public static ProblemDetail of(int status, String title, String detail, URI instance) {
    return new ProblemDetail(ABOUT_BLANK, title, status, detail, instance);
  }
}
