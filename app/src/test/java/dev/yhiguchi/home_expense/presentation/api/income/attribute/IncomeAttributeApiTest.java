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
    given()
        .contentType(ContentType.JSON)
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
  void POST_同名の収入属性を登録すると400エラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "テスト収入属性A更新"}
            """)
        .when()
        .post("/v1/income-attributes")
        .then()
        .statusCode(400);
  }

  @Test
  @Order(6)
  void DELETE_収入属性を削除できる() {
    given().when().delete("/v1/income-attributes/{id}", createdId).then().statusCode(204);
    createdId = null;
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
        .statusCode(400);
  }
}
