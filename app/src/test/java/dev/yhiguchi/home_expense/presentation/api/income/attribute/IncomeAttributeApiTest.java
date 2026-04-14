package dev.yhiguchi.home_expense.presentation.api.income.attribute;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IncomeAttributeApiTest {

  static String createdId;

  @Test
  @Order(1)
  void POST_収入属性を登録できる() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
            {"name": "テスト収入属性A"}
            """)
            .when()
            .post("/v1/income-attributes")
            .then()
            .statusCode(201)
            .header("Location", containsString("/v1/income-attributes/"))
            .extract()
            .header("Location");

    createdId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(2)
  void GET_登録した収入属性を取得できる() {
    given()
        .when()
        .get("/v1/income-attributes/{id}", createdId)
        .then()
        .statusCode(200)
        .header("ETag", notNullValue())
        .body("id", equalTo(createdId))
        .body("name", equalTo("テスト収入属性A"));
  }

  @Test
  @Order(3)
  void GET_収入属性一覧を取得できる() {
    given()
        .when()
        .get("/v1/income-attributes")
        .then()
        .statusCode(200)
        .body("list", is(not(empty())));
  }

  @Test
  @Order(4)
  void PUT_収入属性を更新できる() {
    String etag =
        given()
            .when()
            .get("/v1/income-attributes/{id}", createdId)
            .then()
            .statusCode(200)
            .extract()
            .header("ETag");

    given()
        .contentType(ContentType.JSON)
        .header("If-Match", etag)
        .body(
            """
            {"name": "テスト収入属性A更新"}
            """)
        .when()
        .put("/v1/income-attributes/{id}", createdId)
        .then()
        .statusCode(204);

    given()
        .when()
        .get("/v1/income-attributes/{id}", createdId)
        .then()
        .statusCode(200)
        .body("name", equalTo("テスト収入属性A更新"));
  }

  @Test
  @Order(5)
  void POST_同名の収入属性を登録すると409エラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "テスト収入属性A更新"}
            """)
        .when()
        .post("/v1/income-attributes")
        .then()
        .statusCode(409)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Conflict"))
        .body("status", equalTo(409))
        .body("detail", equalTo("既に登録されています"))
        .body("instance", containsString("/v1/income-attributes"));
  }

  @Test
  @Order(6)
  void DELETE_収入属性を削除できる() {
    given().when().delete("/v1/income-attributes/{id}", createdId).then().statusCode(204);
    createdId = null;
  }

  @Test
  void GET_存在しない収入属性を取得すると404エラー() {
    given()
        .when()
        .get("/v1/income-attributes/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(404)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Not Found"))
        .body("status", equalTo(404))
        .body("detail", equalTo("収入属性が見つかりません"));
  }

  @Test
  void PUT_存在しない収入属性を更新すると404エラー() {
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"name": "テスト"}
            """)
        .when()
        .put("/v1/income-attributes/{id}", "00000000-0000-0000-0000-000000000000")
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
        .get("/v1/income-attributes")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", containsString("per_pageは100以下を指定してください"))
        .body("instance", containsString("/v1/income-attributes"));
  }

  @Test
  void POST_nameが512文字を超える場合バリデーションエラー() {
    String longName = "あ".repeat(513);
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "%s"}
            """
                .formatted(longName))
        .when()
        .post("/v1/income-attributes")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("nameは512文字以内で入力してください"));
  }

  @Test
  void PUT_nameが512文字を超える場合バリデーションエラー() {
    String longName = "あ".repeat(513);
    given()
        .contentType(ContentType.JSON)
        .header("If-Match", "\"1\"")
        .body(
            """
            {"name": "%s"}
            """
                .formatted(longName))
        .when()
        .put("/v1/income-attributes/{id}", "00000000-0000-0000-0000-000000000000")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("status", equalTo(400))
        .body("detail", containsString("nameは512文字以内で入力してください"));
  }

  @Test
  void POST_nameが空の場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": ""}
            """)
        .when()
        .post("/v1/income-attributes")
        .then()
        .statusCode(400)
        .contentType("application/problem+json")
        .body("type", equalTo("about:blank"))
        .body("title", equalTo("Bad Request"))
        .body("status", equalTo(400))
        .body("detail", notNullValue())
        .body("instance", containsString("/v1/income-attributes"));
  }
}
