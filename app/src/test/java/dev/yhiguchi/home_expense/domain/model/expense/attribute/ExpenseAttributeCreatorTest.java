package dev.yhiguchi.home_expense.domain.model.expense.attribute;

import static org.junit.jupiter.api.Assertions.*;

import dev.yhiguchi.home_expense.domain.model.expense.ExpenseCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ExpenseAttributeCreatorTest {

  @Test
  void 経費属性を作成して登録する() {
    List<ExpenseAttribute> registered = new ArrayList<>();

    ExpenseAttributeCreator creator =
        new ExpenseAttributeCreator(name -> Optional.empty(), registered::add);

    ExpenseAttribute attribute =
        creator.createAndRegister(new ExpenseAttributeName("食費"), ExpenseCategory.変動費);

    assertNotNull(attribute.expenseAttributeIdentifier().value());
    assertEquals("食費", attribute.expenseAttributeName().value());
    assertEquals(ExpenseCategory.変動費, attribute.expenseCategory());
    assertEquals(1, registered.size());
  }

  @Test
  void 同名の経費属性が存在する場合は例外をスローする() {
    ExpenseAttribute existing =
        new ExpenseAttribute(
            new ExpenseAttributeIdentifier("attr-1"),
            new ExpenseAttributeName("食費"),
            ExpenseCategory.変動費);

    ExpenseAttributeCreator creator =
        new ExpenseAttributeCreator(name -> Optional.of(existing), attribute -> {});

    assertThrows(
        ExpenseAttributeAlreadyExistsException.class,
        () -> creator.createAndRegister(new ExpenseAttributeName("食費"), ExpenseCategory.変動費));
  }
}
