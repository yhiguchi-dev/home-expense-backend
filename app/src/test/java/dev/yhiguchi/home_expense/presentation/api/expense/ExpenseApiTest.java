package dev.yhiguchi.home_expense.presentation.api.expense;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExpenseApiTest {

  static String attributeId;
  static String expenseId;

  @Test
  @Order(0)
  void セットアップ_経費属性を作成する() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
                {"name": "テスト食費", "category": "変動費"}
                """)
            .when()
            .post("/v1/expense-attributes")
            .then()
            .statusCode(201)
            .extract()
            .header("Location");
    attributeId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(1)
  void POST_経費を登録できる() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
                {"description": "ランチ", "price": 1000, "payment_date": "2026-04-10", "attribute_id": "%s"}
                """
                    .formatted(attributeId))
            .when()
            .post("/v1/expenses")
            .then()
            .statusCode(201)
            .header("Location", containsString("/v1/expenses/"))
            .extract()
            .header("Location");

    expenseId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(2)
  void GET_登録した経費を取得できる() {
    given()
        .when()
        .get("/v1/expenses/{id}", expenseId)
        .then()
        .statusCode(200)
        .body("id", equalTo(expenseId))
        .body("description", equalTo("ランチ"))
        .body("price", equalTo(1000))
        .body("payment_date", equalTo("2026-04-10"))
        .body("expense_attribute.id", equalTo(attributeId));
  }

  @Test
  @Order(3)
  void GET_経費一覧を取得できる() {
    given().when().get("/v1/expenses").then().statusCode(200).body("list", is(not(empty())));
  }

  @Test
  @Order(4)
  void PUT_経費を更新できる() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "ディナー", "price": 3000, "payment_date": "2026-04-10", "attribute_id": "%s"}
            """
                .formatted(attributeId))
        .when()
        .put("/v1/expenses/{id}", expenseId)
        .then()
        .statusCode(204);

    given()
        .when()
        .get("/v1/expenses/{id}", expenseId)
        .then()
        .statusCode(200)
        .body("description", equalTo("ディナー"))
        .body("price", equalTo(3000));
  }

  @Test
  @Order(5)
  void DELETE_経費を削除できる() {
    given().when().delete("/v1/expenses/{id}", expenseId).then().statusCode(204);
    expenseId = null;
  }

  @Test
  @Order(6)
  void クリーンアップ_経費属性を削除する() {
    given().when().delete("/v1/expense-attributes/{id}", attributeId).then().statusCode(204);
    attributeId = null;
  }

  @Test
  void POST_必須項目が欠けている場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": null, "price": null, "payment_date": "", "attribute_id": ""}
            """)
        .when()
        .post("/v1/expenses")
        .then()
        .statusCode(400);
  }
}
