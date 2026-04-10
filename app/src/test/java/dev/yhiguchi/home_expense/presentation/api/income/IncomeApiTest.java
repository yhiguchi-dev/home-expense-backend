package dev.yhiguchi.home_expense.presentation.api.income;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IncomeApiTest {

  static String attributeId;
  static String incomeId;

  @Test
  @Order(0)
  void セットアップ_収入属性を作成する() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
                {"name": "テスト給与"}
                """)
            .when()
            .post("/v1/income-attributes")
            .then()
            .statusCode(201)
            .extract()
            .header("Location");
    attributeId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(1)
  void POST_収入を登録できる() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
                {"description": "4月給与", "amount": 300000, "receive_date": "2026-04-25", "attribute_id": "%s"}
                """
                    .formatted(attributeId))
            .when()
            .post("/v1/incomes")
            .then()
            .statusCode(201)
            .header("Location", containsString("/v1/incomes/"))
            .extract()
            .header("Location");

    incomeId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(2)
  void GET_登録した収入を取得できる() {
    given()
        .when()
        .get("/v1/incomes/{id}", incomeId)
        .then()
        .statusCode(200)
        .body("id", equalTo(incomeId))
        .body("description", equalTo("4月給与"))
        .body("amount", equalTo(300000))
        .body("receive_date", equalTo("2026-04-25"))
        .body("income_attribute.id", equalTo(attributeId));
  }

  @Test
  @Order(3)
  void GET_収入一覧を取得できる() {
    given().when().get("/v1/incomes").then().statusCode(200).body("list", is(not(empty())));
  }

  @Test
  @Order(4)
  void PUT_収入を更新できる() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "4月給与（修正）", "amount": 350000, "receive_date": "2026-04-25", "attribute_id": "%s"}
            """
                .formatted(attributeId))
        .when()
        .put("/v1/incomes/{id}", incomeId)
        .then()
        .statusCode(204);

    given()
        .when()
        .get("/v1/incomes/{id}", incomeId)
        .then()
        .statusCode(200)
        .body("description", equalTo("4月給与（修正）"))
        .body("amount", equalTo(350000));
  }

  @Test
  @Order(5)
  void DELETE_収入を削除できる() {
    given().when().delete("/v1/incomes/{id}", incomeId).then().statusCode(204);
    incomeId = null;
  }

  @Test
  @Order(6)
  void クリーンアップ_収入属性を削除する() {
    given().when().delete("/v1/income-attributes/{id}", attributeId).then().statusCode(204);
    attributeId = null;
  }

  @Test
  void POST_必須項目が欠けている場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": null, "amount": null, "receive_date": "", "attribute_id": ""}
            """)
        .when()
        .post("/v1/incomes")
        .then()
        .statusCode(400);
  }
}
