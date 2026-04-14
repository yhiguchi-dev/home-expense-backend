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
        .header("ETag", notNullValue())
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
    String etag =
        given()
            .when()
            .get("/v1/incomes/{id}", incomeId)
            .then()
            .statusCode(200)
            .extract()
            .header("ETag");

    given()
        .contentType(ContentType.JSON)
        .header("If-Match", etag)
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
  void GET_存在しない収入を取得すると404エラー() {
    given()
        .when()
        .get("/v1/incomes/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Not Found"))
        .body("status", equalTo(404))
        .body("detail", equalTo("収入が見つかりません"));
  }

  @Test
  void PUT_存在しない収入を更新すると404エラー() {
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"description": "テスト", "amount": 100000, "receive_date": "2026-04-25", "attribute_id": "00000000-0000-0000-0000-000000000001"}
            """)
        .when()
        .put("/v1/incomes/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("status", equalTo(404))
        .body("detail", equalTo("収入が見つかりません"));
  }

  @Test
  void POST_存在しない属性IDで収入を登録すると404エラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "テスト", "amount": 100000, "receive_date": "2026-04-25", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """)
        .when()
        .post("/v1/incomes")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("status", equalTo(404))
        .body("detail", equalTo("収入属性が見つかりません"));
  }

  @Test
  void GET_per_pageが上限を超える場合バリデーションエラー() {
    given()
        .queryParam("per_page", 101)
        .when()
        .get("/v1/incomes")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", containsString("per_pageは100以下を指定してください"))
        .body("instance", containsString("/v1/incomes"));
  }

  @Test
  void POST_descriptionが512文字を超える場合バリデーションエラー() {
    String longDescription = "あ".repeat(513);
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"description": "%s", "amount": 300000, "receive_date": "2026-04-25", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """
                .formatted(longDescription))
        .when()
        .post("/v1/incomes")
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
            {"description": "4月給与", "amount": 300000, "receive_date": "2026-04-25", "attribute_id": "invalid-uuid"}
            """)
        .when()
        .post("/v1/incomes")
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
            {"description": "%s", "amount": 300000, "receive_date": "2026-04-25", "attribute_id": "00000000-0000-0000-0000-000000000000"}
            """
                .formatted(longDescription))
        .when()
        .put("/v1/incomes/{id}", "00000000-0000-0000-0000-000000000000")
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
            {"description": "4月給与", "amount": 300000, "receive_date": "2026-04-25", "attribute_id": "not-a-valid-uuid!"}
            """)
        .when()
        .put("/v1/incomes/{id}", "00000000-0000-0000-0000-000000000000")
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
            {"description": null, "amount": null, "receive_date": "", "attribute_id": ""}
            """)
        .when()
        .post("/v1/incomes")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", notNullValue())
        .body("instance", containsString("/v1/incomes"));
  }
}
