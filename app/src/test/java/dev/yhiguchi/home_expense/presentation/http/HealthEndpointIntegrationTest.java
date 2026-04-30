package dev.yhiguchi.home_expense.presentation.http;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HealthEndpointIntegrationTest {

  @Test
  void liveness_エンドポイントが_UP_を返す() {
    given().when().get("/q/health/live").then().statusCode(200).body("status", equalTo("UP"));
  }

  @Test
  void readiness_エンドポイントが_UP_を返す() {
    given().when().get("/q/health/ready").then().statusCode(200).body("status", equalTo("UP"));
  }

  @Test
  void startup_エンドポイントが_UP_を返す() {
    given().when().get("/q/health/started").then().statusCode(200).body("status", equalTo("UP"));
  }

  @Test
  void 集約エンドポイントが_UP_を返す() {
    given().when().get("/q/health").then().statusCode(200).body("status", equalTo("UP"));
  }

  @Test
  void readiness_にデータソース接続性チェックが含まれる() {
    given()
        .when()
        .get("/q/health/ready")
        .then()
        .statusCode(200)
        .body("checks.name", org.hamcrest.Matchers.hasItem("Database connections health check"));
  }
}
