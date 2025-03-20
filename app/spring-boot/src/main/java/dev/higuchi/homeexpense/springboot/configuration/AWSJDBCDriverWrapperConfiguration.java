package dev.higuchi.homeexpense.springboot.configuration;

import com.zaxxer.hikari.HikariConfig;
import java.util.Arrays;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.jdbc.HikariPooledConnectionProvider;
import software.amazon.jdbc.HostSpec;
import software.amazon.jdbc.plugin.failover2.FailoverConnectionPluginFactory;
import software.amazon.jdbc.plugin.readwritesplitting.ReadWriteSplittingPluginFactory;
import software.amazon.jdbc.profile.ConfigurationProfileBuilder;

@Component
@Profile("aws")
public class AWSJDBCDriverWrapperConfiguration implements InitializingBean {
  HikariConfig hikariConfig;

  public AWSJDBCDriverWrapperConfiguration(HikariConfig hikariConfig) {
    this.hikariConfig = hikariConfig;
  }

  @Override
  public void afterPropertiesSet() {
    ConfigurationProfileBuilder.get()
        .withName("datasource-with-internal-connection-pool")
        .withPluginFactories(
            Arrays.asList(
                FailoverConnectionPluginFactory.class, ReadWriteSplittingPluginFactory.class))
        .withConnectionProvider(
            new HikariPooledConnectionProvider(
                (HostSpec hostSpec, Properties originalProps) -> hikariConfig, null))
        .buildAndSet();
  }
}
