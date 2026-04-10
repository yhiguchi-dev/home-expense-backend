package dev.yhiguchi.home_expense.presentation.api.expense.attribute;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExpenseAttributeApiTest {

  static String createdId;

  @Test
  @Order(1)
  void POST_経費属性を登録できる() {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(
                """
            {"name": "テスト属性A", "category": "変動費"}
            """)
            .when()
            .post("/v1/expense-attributes")
            .then()
            .statusCode(201)
            .header("Location", containsString("/v1/expense-attributes/"))
            .extract()
            .header("Location");

    createdId = location.substring(location.lastIndexOf("/") + 1);
  }

  @Test
  @Order(2)
  void GET_登録した経費属性を取得できる() {
    given()
        .when()
        .get("/v1/expense-attributes/{id}", createdId)
        .then()
        .statusCode(200)
        .body("id", equalTo(createdId))
        .body("name", equalTo("テスト属性A"))
        .body("category", equalTo("変動費"));
  }

  @Test
  @Order(3)
  void GET_経費属性一覧を取得できる() {
    given()
        .when()
        .get("/v1/expense-attributes")
        .then()
        .statusCode(200)
        .body("list", is(not(empty())));
  }

  @Test
  @Order(4)
  void PUT_経費属性を更新できる() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "テスト属性A更新", "category": "変動費"}
            """)
        .when()
        .put("/v1/expense-attributes/{id}", createdId)
        .then()
        .statusCode(204);

    given()
        .when()
        .get("/v1/expense-attributes/{id}", createdId)
        .then()
        .statusCode(200)
        .body("name", equalTo("テスト属性A更新"));
  }

  @Test
  @Order(5)
  void POST_同名の経費属性を登録すると400エラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "テスト属性A更新", "category": "変動費"}
            """)
        .when()
        .post("/v1/expense-attributes")
        .then()
        .statusCode(400);
  }

  @Test
  @Order(6)
  void DELETE_経費属性を削除できる() {
    given().when().delete("/v1/expense-attributes/{id}", createdId).then().statusCode(204);
    createdId = null;
  }

  @Test
  void POST_nameが空の場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "", "category": "変動費"}
            """)
        .when()
        .post("/v1/expense-attributes")
        .then()
        .statusCode(400);
  }

  @Test
  void POST_categoryが不正な場合バリデーションエラー() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {"name": "テスト", "category": "不正値"}
            """)
        .when()
        .post("/v1/expense-attributes")
        .then()
        .statusCode(400);
  }
}
