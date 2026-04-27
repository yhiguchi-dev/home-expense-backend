package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ExpenseAttributeNameUniquenessTest {

  @Nested
  class 新規登録時 {

    @Test
    void 同じ名前と分類の組合せが存在しない場合は成功する() {
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> false);

      assertDoesNotThrow(
          () ->
              sut.assertUniqueForRegistration(new ExpenseAttributeName("食費"), ExpenseCategory.変動費));
    }

    @Test
    void 同じ名前と分類の組合せが存在する場合は例外をスローする() {
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> true);

      assertThrows(
          ExpenseAttributeAlreadyExistsException.class,
          () ->
              sut.assertUniqueForRegistration(new ExpenseAttributeName("食費"), ExpenseCategory.変動費));
    }

    @Test
    void 名前のみ一致し分類が異なる場合は許可する() {
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> category == ExpenseCategory.固定費);

      assertDoesNotThrow(
          () ->
              sut.assertUniqueForRegistration(new ExpenseAttributeName("食費"), ExpenseCategory.変動費));
    }
  }

  @Nested
  class 更新時 {

    @Test
    void 名前も分類も変わっていない場合は検査をスキップする() {
      ExpenseAttribute current =
          new ExpenseAttribute(
              new ExpenseAttributeIdentifier("attr-1"),
              new ExpenseAttributeName("食費"),
              ExpenseCategory.変動費);
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness(
              (name, category) -> {
                throw new AssertionError("検査されるべきでない");
              });

      assertDoesNotThrow(
          () ->
              sut.assertUniqueForUpdate(
                  current, new ExpenseAttributeName("食費"), ExpenseCategory.変動費));
    }

    @Test
    void 名前を変更し新しい組合せが存在しない場合は成功する() {
      ExpenseAttribute current =
          new ExpenseAttribute(
              new ExpenseAttributeIdentifier("attr-1"),
              new ExpenseAttributeName("食費"),
              ExpenseCategory.変動費);
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> false);

      assertDoesNotThrow(
          () ->
              sut.assertUniqueForUpdate(
                  current, new ExpenseAttributeName("交通費"), ExpenseCategory.変動費));
    }

    @Test
    void 名前を変更し新しい組合せが他に存在する場合は例外をスローする() {
      ExpenseAttribute current =
          new ExpenseAttribute(
              new ExpenseAttributeIdentifier("attr-1"),
              new ExpenseAttributeName("食費"),
              ExpenseCategory.変動費);
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> true);

      assertThrows(
          ExpenseAttributeAlreadyExistsException.class,
          () ->
              sut.assertUniqueForUpdate(
                  current, new ExpenseAttributeName("交通費"), ExpenseCategory.変動費));
    }

    @Test
    void 分類だけを変更し新しい組合せが他に存在する場合は例外をスローする() {
      ExpenseAttribute current =
          new ExpenseAttribute(
              new ExpenseAttributeIdentifier("attr-1"),
              new ExpenseAttributeName("食費"),
              ExpenseCategory.変動費);
      ExpenseAttributeNameUniqueness sut =
          new ExpenseAttributeNameUniqueness((name, category) -> true);

      assertThrows(
          ExpenseAttributeAlreadyExistsException.class,
          () ->
              sut.assertUniqueForUpdate(
                  current, new ExpenseAttributeName("食費"), ExpenseCategory.固定費));
    }
  }
}
