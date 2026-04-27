package dev.yhiguchi.home_expense.presentation.problem;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TraceContextIntegrationTest {

  private static final String TRACEPARENT_PATTERN = "^00-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}$";
  private static final String TRACE_ID_PATTERN = "^[0-9a-f]{32}$";

  @Test
  void エラー応答のbodyにtrace_idが含まれる() {
    given()
        .when()
        .get("/v1/expenses?per_page=999")
        .then()
        .statusCode(400)
        .body("trace_id", matchesPattern(TRACE_ID_PATTERN));
  }

  @Test
  void エラー応答にtraceresponseヘッダが含まれる() {
    given()
        .when()
        .get("/v1/expenses?per_page=999")
        .then()
        .statusCode(400)
        .header("traceresponse", matchesPattern(TRACEPARENT_PATTERN));
  }

  @Test
  void 正常応答にtraceresponseヘッダが含まれる() {
    given()
        .when()
        .get("/v1/expenses")
        .then()
        .statusCode(200)
        .header("traceresponse", matchesPattern(TRACEPARENT_PATTERN));
  }

  @Test
  void inboundのtraceparentがエラー応答のtrace_idに反映される() {
    String inboundTraceId = "0af7651916cd43dd8448eb211c80319c";
    String traceparent = "00-" + inboundTraceId + "-b7ad6b7169203331-01";

    given()
        .header("traceparent", traceparent)
        .when()
        .get("/v1/expenses?per_page=999")
        .then()
        .statusCode(400)
        .body("trace_id", equalTo(inboundTraceId))
        .header("traceresponse", startsWith("00-" + inboundTraceId + "-"));
  }
}
