package dev.yhiguchi.home_expense.query;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaginationTest {

  @Test
  void offset_1ページ目() {
    Pagination pagination = new Pagination(new Page(1), new PerPage(20));
    assertEquals(0, pagination.offset());
  }

  @Test
  void offset_2ページ目() {
    Pagination pagination = new Pagination(new Page(2), new PerPage(20));
    assertEquals(20, pagination.offset());
  }

  @Test
  void offset_3ページ目() {
    Pagination pagination = new Pagination(new Page(3), new PerPage(10));
    assertEquals(20, pagination.offset());
  }

  @Test
  void lastPage_割り切れる場合() {
    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    assertEquals(5, pagination.lastPage(50));
  }

  @Test
  void lastPage_余りがある場合() {
    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    assertEquals(6, pagination.lastPage(51));
  }

  @Test
  void lastPage_件数が0の場合() {
    Pagination pagination = new Pagination(new Page(1), new PerPage(10));
    assertEquals(0, pagination.lastPage(0));
  }

  @Test
  void isFirstPage() {
    assertTrue(new Pagination(new Page(1), new PerPage(10)).isFirstPage());
    assertFalse(new Pagination(new Page(2), new PerPage(10)).isFirstPage());
  }

  @Test
  void isLastPage() {
    Pagination pagination = new Pagination(new Page(3), new PerPage(10));
    assertTrue(pagination.isLastPage(30));
    assertFalse(pagination.isLastPage(31));
  }
}
