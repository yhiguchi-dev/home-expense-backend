package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IncomeAttributeNameUniquenessTest {

  @Nested
  class 新規登録時 {

    @Test
    void 同名が存在しない場合は成功する() {
      IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> false);

      assertDoesNotThrow(() -> sut.assertUniqueForRegistration(new IncomeAttributeName("給与")));
    }

    @Test
    void 同名が存在する場合は例外をスローする() {
      IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> true);

      assertThrows(
          IncomeAttributeAlreadyExistsException.class,
          () -> sut.assertUniqueForRegistration(new IncomeAttributeName("給与")));
    }
  }

  @Nested
  class 更新時 {

    @Test
    void 名前が変わっていない場合は検査をスキップする() {
      IncomeAttribute current =
          new IncomeAttribute(
              new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
      IncomeAttributeNameUniqueness sut =
          new IncomeAttributeNameUniqueness(
              name -> {
                throw new AssertionError("検査されるべきでない");
              });

      assertDoesNotThrow(() -> sut.assertUniqueForUpdate(current, new IncomeAttributeName("給与")));
    }

    @Test
    void 名前を変更し新しい名前が存在しない場合は成功する() {
      IncomeAttribute current =
          new IncomeAttribute(
              new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
      IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> false);

      assertDoesNotThrow(() -> sut.assertUniqueForUpdate(current, new IncomeAttributeName("賞与")));
    }

    @Test
    void 名前を変更し新しい名前が他に存在する場合は例外をスローする() {
      IncomeAttribute current =
          new IncomeAttribute(
              new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));
      IncomeAttributeNameUniqueness sut = new IncomeAttributeNameUniqueness(name -> true);

      assertThrows(
          IncomeAttributeAlreadyExistsException.class,
          () -> sut.assertUniqueForUpdate(current, new IncomeAttributeName("賞与")));
    }
  }
}
