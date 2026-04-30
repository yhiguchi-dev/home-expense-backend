package dev.yhiguchi.home_expense.presentation.api.expense;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ExpenseStatisticsApiTest {

  @Test
  void GET_経費統計を取得できる() {
    given()
        .queryParam("year", 2026)
        .queryParam("month", 4)
        .when()
        .get("/v1/expense-statistics")
        .then()
        .statusCode(200)
        .body("income_total_amount", notNullValue())
        .body("total_amount", notNullValue())
        .body("fixed_expense_detail", notNullValue())
        .body("variable_expense_detail", notNullValue());
  }

  @Test
  void GET_yearが0以下の場合は400() {
    given()
        .queryParam("year", 0)
        .queryParam("month", 4)
        .when()
        .get("/v1/expense-statistics")
        .then()
        .statusCode(400);
  }

  @Test
  void GET_monthが範囲外の場合は400() {
    given()
        .queryParam("year", 2026)
        .queryParam("month", 13)
        .when()
        .get("/v1/expense-statistics")
        .then()
        .statusCode(400);
  }
}
