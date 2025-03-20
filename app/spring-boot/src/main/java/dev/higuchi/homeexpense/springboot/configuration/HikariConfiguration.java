package dev.higuchi.homeexpense.springboot.configuration;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class HikariConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource.configuration")
  HikariConfig hikariConfig() {
    return new HikariConfig();
  }
}
