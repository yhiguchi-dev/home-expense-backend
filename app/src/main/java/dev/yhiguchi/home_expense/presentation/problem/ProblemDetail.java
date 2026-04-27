package dev.yhiguchi.home_expense.presentation.problem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/** RFC 9457 Problem Details for HTTP APIs (W3C Trace Context 拡張付き) */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
    @JsonProperty("type") URI type,
    @JsonProperty("title") String title,
    @JsonProperty("status") int status,
    @JsonProperty("detail") String detail,
    @JsonProperty("instance") URI instance,
    @JsonProperty("trace_id") String traceId) {

  private static final URI ABOUT_BLANK = URI.create("about:blank");

  public static ProblemDetail of(
      int status, String title, String detail, URI instance, String traceId) {
    return new ProblemDetail(ABOUT_BLANK, title, status, detail, instance, traceId);
  }
}
