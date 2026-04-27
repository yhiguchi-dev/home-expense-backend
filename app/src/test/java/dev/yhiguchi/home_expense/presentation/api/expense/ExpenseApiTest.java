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
                {"description": "ランチ", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "%s"}
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
        .header("ETag", notNullValue())
        .body("id", equalTo(expenseId))
        .body("description", equalTo("ランチ"))
        .body("amount", equalTo(1000))
        .body("payment_date", equalTo("2026-04-10"))
        .body("expense_attribute_id", equalTo(attributeId));
  }

  @Test
  @Order(3)
  void GET_経費一覧を取得できる() {
    given().when().get("/v1/expenses").then().statusCode(200).body("list", is(not(empty())));
  }

  @Test
  @Order(4)
  void PUT_経費を更新できる() {
    String etag =
        given()
            .when()
            .get("/v1/expenses/{id}", expenseId)
            .then()
            .statusCode(200)
            .extract()
            .header("ETag");

    given()
        .contentType(ContentType.JSON)
        .header("If-Match", etag)
        .body(
            """
            {"description": "ディナー", "amount": 3000, "payment_date": "2026-04-10", "attribute_id": "%s"}
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
        .body("amount", equalTo(3000));
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
  void GET_存在しない経費を取得すると404エラー() {
    given()
        .when()
        .get("/v1/expenses/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Not Found"))
        .body("status", equalTo(404))
        .body("detail", equalTo("経費が見つかりません"));
  }

  @Test
  void PUT_存在しない経費を更新すると404エラー() {
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"description": "テスト", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "00000000-0000-0000-0000-000000000001"}
            """)
        .when()
        .put("/v1/expenses/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("status", equalTo(404))
        .body("detail", equalTo("経費が見つかりません"));
  }

  @Test
  void POST_存在しない属性IDで経費を登録すると404エラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "テスト", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """)
        .when()
        .post("/v1/expenses")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("status", equalTo(404))
        .body("detail", equalTo("経費属性が見つかりません"));
  }

  @Test
  void GET_per_pageが上限を超える場合バリデーションエラー() {
    given()
        .queryParam("per_page", 101)
        .when()
        .get("/v1/expenses")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", containsString("per_pageは100以下を指定してください"))
        .body("instance", containsString("/v1/expenses"));
  }

  @Test
  void POST_descriptionが512文字を超える場合バリデーションエラー() {
    String longDescription = "あ".repeat(513);
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "%s", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """
                .formatted(longDescription))
        .when()
        .post("/v1/expenses")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("descriptionは512文字以内で入力してください"));
  }

  @Test
  void POST_attribute_idがUUID形式でない場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "ランチ", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "invalid-uuid"}
            """)
        .when()
        .post("/v1/expenses")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("attribute_idの形式に誤りがあります"));
  }

  @Test
  void PUT_descriptionが512文字を超える場合バリデーションエラー() {
    String longDescription = "あ".repeat(513);
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"description": "%s", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """
                .formatted(longDescription))
        .when()
        .put("/v1/expenses/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("descriptionは512文字以内で入力してください"));
  }

  @Test
  void PUT_attribute_idがUUID形式でない場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"description": "ランチ", "amount": 1000, "payment_date": "2026-04-10", "attribute_id": "not-a-valid-uuid!"}
            """)
        .when()
        .put("/v1/expenses/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("attribute_idの形式に誤りがあります"));
  }

  @Test
  void POST_必須項目が欠けている場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": null, "amount": null, "payment_date": "", "attribute_id": ""}
            """)
        .when()
        .post("/v1/expenses")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", notNullValue())
        .body("instance", containsString("/v1/expenses"));
  }
}
