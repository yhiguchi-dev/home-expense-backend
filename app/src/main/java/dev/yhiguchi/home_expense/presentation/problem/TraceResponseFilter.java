package dev.yhiguchi.home_expense.presentation.problem;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/** W3C Trace Context Level 2 の traceresponse ヘッダを全レスポンスに付与する */
@Provider
public class TraceResponseFilter implements ContainerResponseFilter {

  private static final String HEADER = "traceresponse";
  private static final String VERSION = "00";

  @Override
  public void filter(ContainerRequestContext req, ContainerResponseContext res) {
    SpanContext ctx = Span.current().getSpanContext();
    if (!ctx.isValid()) return;
    String value =
        "%s-%s-%s-%s"
            .formatted(VERSION, ctx.getTraceId(), ctx.getSpanId(), ctx.getTraceFlags().asHex());
    res.getHeaders().putSingle(HEADER, value);
  }
}
