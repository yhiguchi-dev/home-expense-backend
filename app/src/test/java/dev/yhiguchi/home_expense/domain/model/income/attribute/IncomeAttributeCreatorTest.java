package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class IncomeAttributeCreatorTest {

  @Test
  void 収入属性を作成して登録する() {
    List<IncomeAttribute> registered = new ArrayList<>();

    IncomeAttributeCreator creator =
        new IncomeAttributeCreator(name -> Optional.empty(), registered::add);

    IncomeAttribute attribute = creator.createAndRegister(new IncomeAttributeName("給与"));

    assertNotNull(attribute.incomeAttributeIdentifier().value());
    assertEquals("給与", attribute.incomeAttributeName().value());
    assertEquals(1, registered.size());
  }

  @Test
  void 同名の収入属性が存在する場合は例外をスローする() {
    IncomeAttribute existing =
        new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    IncomeAttributeCreator creator =
        new IncomeAttributeCreator(name -> Optional.of(existing), attribute -> {});

    assertThrows(
        IncomeAttributeAlreadyExistsException.class,
        () -> creator.createAndRegister(new IncomeAttributeName("給与")));
  }
}
