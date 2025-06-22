package dev.higuchi.homeexpense.postgresql.mybatisadapter;

import dev.higuchi.homeexpense.command.model.expense.ExpenseCategory;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttribute;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeIdentifier;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeName;
import dev.higuchi.homeexpense.command.model.expense.attribute.ExpenseAttributeNotFoundException;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeDataSource;
import dev.higuchi.homeexpense.postgresql.mybatisadapter.datasource.expense.attribute.ExpenseAttributeMapper;
import java.util.UUID;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExpenseAggregateDataSourceTest implements MybatisConfigurer {
  @Test
  public void test() {
    Configuration configuration = configuration("localhost", 5432);
    configuration.addMapper(ExpenseAttributeMapper.class);
    try (SqlSession sqlSession = openSession(configuration)) {
      ExpenseAttributeMapper mapper = sqlSession.getMapper(ExpenseAttributeMapper.class);
      ExpenseAttributeDataSource dataSource = new ExpenseAttributeDataSource(mapper);
      ExpenseAttribute expenseAttribute =
          new ExpenseAttribute(
              new ExpenseAttributeIdentifier(UUID.randomUUID().toString()),
              new ExpenseAttributeName("test"),
              ExpenseCategory.固定費);
      dataSource.register(expenseAttribute);
      ExpenseAttribute actual = dataSource.get(expenseAttribute.expenseAttributeIdentifier());
      Assertions.assertEquals(expenseAttribute, actual);
      Assertions.assertThrows(
          ExpenseAttributeNotFoundException.class,
          () -> dataSource.get(new ExpenseAttributeIdentifier(UUID.randomUUID().toString())));
      ExpenseAttribute find = dataSource.find(expenseAttribute.expenseAttributeName());
      Assertions.assertEquals(expenseAttribute, find);
      ExpenseAttribute findNotFound = dataSource.find(new ExpenseAttributeName("notFound"));
      Assertions.assertEquals(new ExpenseAttribute(), findNotFound);
      ExpenseAttribute updated =
          new ExpenseAttribute(
              expenseAttribute.expenseAttributeIdentifier(),
              new ExpenseAttributeName("updated"),
              ExpenseCategory.変動費);
      dataSource.update(updated);
      ExpenseAttribute updatedActual =
          dataSource.get(expenseAttribute.expenseAttributeIdentifier());
      Assertions.assertEquals(updated, updatedActual);
      dataSource.delete(updated.expenseAttributeIdentifier());
      Assertions.assertThrows(
          ExpenseAttributeNotFoundException.class,
          () -> dataSource.get(updated.expenseAttributeIdentifier()));
    }
  }
}
