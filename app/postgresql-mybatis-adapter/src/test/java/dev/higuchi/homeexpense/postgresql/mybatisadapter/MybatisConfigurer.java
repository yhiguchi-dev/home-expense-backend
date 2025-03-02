package dev.higuchi.homeexpense.postgresql.mybatisadapter;

import javax.sql.DataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

interface MybatisConfigurer {

  default Configuration configuration() {
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    DataSource pooledDataSource =
        new PooledDataSource(
            "org.postgresql.Driver",
            "jdbc:postgresql://localhost:5432/expense",
            "expense",
            "password");
    Environment environment = new Environment("test", transactionFactory, pooledDataSource);
    return new Configuration(environment);
  }

  default SqlSession openSession(Configuration configuration) {
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    return sqlSessionFactory.openSession();
  }
}
