package dev.higuchi.homeexpense.springboot.configuration;

import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfiguration {

  @Bean
  SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer() {
    return factoryBean -> {
      //            factoryBean.addTypeHandlers(new ArrayTypeHandler());
    };
  }
}
