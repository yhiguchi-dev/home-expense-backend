package dev.yhiguchi.home_expense.domain.model.income.attribute;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class IncomeAttributeUpdaterTest {

  IncomeAttribute existing =
      new IncomeAttribute(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

  @Test
  void 収入属性を更新する() {
    List<IncomeAttribute> updated = new ArrayList<>();

    IncomeAttributeUpdater updater = new IncomeAttributeUpdater(id -> existing, updated::add);

    updater.update(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("賞与"));

    assertEquals(1, updated.size());
    assertEquals("賞与", updated.getFirst().incomeAttributeName().value());
  }

  @Test
  void 変更がない場合は更新しない() {
    List<IncomeAttribute> updated = new ArrayList<>();

    IncomeAttributeUpdater updater = new IncomeAttributeUpdater(id -> existing, updated::add);

    updater.update(new IncomeAttributeIdentifier("attr-1"), new IncomeAttributeName("給与"));

    assertTrue(updated.isEmpty());
  }
}
