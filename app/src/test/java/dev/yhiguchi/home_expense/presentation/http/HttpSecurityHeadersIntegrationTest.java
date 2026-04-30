package dev.yhiguchi.home_expense.presentation.http;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HttpSecurityHeadersIntegrationTest {

  @Test
  void v1配下のレスポンスにCacheControl_no_storeが付与される() {
    given().when().get("/v1/expenses").then().header("Cache-Control", equalTo("no-store"));
  }

  @Test
  void healthエンドポイントには既存のCacheControl_no_storeが維持される() {
    given().when().get("/q/health/live").then().header("Cache-Control", equalTo("no-store"));
  }
}
