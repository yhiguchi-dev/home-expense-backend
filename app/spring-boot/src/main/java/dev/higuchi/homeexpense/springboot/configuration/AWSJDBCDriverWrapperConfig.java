package dev.higuchi.homeexpense.springboot.configuration;

import com.zaxxer.hikari.HikariConfig;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.jdbc.HikariPooledConnectionProvider;
import software.amazon.jdbc.HostSpec;
import software.amazon.jdbc.dialect.AuroraPgDialect;
import software.amazon.jdbc.profile.ConfigurationProfileBuilder;
import software.amazon.jdbc.profile.ConfigurationProfilePresetCodes;

@Component
@Profile("aws")
public class AWSJDBCDriverWrapperConfig implements InitializingBean {
  @Override
  public void afterPropertiesSet() {
    System.out.println("Hello, world!");
    ConfigurationProfileBuilder.get()
        .from(ConfigurationProfilePresetCodes.F0)
        .withName("datasource-with-internal-connection-pool")
        .withConnectionProvider(
            new HikariPooledConnectionProvider(
                (HostSpec hostSpec, Properties originalProps) -> {
                  final HikariConfig config = new HikariConfig();
                  config.setMaximumPoolSize(30);
                  // holds few extra connections in case of sudden traffic peak
                  config.setMinimumIdle(2);
                  // close idle connection in 15min; helps to get back to normal pool size after
                  // load peak
                  config.setIdleTimeout(TimeUnit.MINUTES.toMillis(15));
                  // verify pool configuration and creates no connections during initialization
                  // phase
                  config.setInitializationFailTimeout(-1);
                  config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
                  // validate idle connections at least every 3 min
                  config.setKeepaliveTime(TimeUnit.MINUTES.toMillis(3));
                  // allows to quickly validate connection in the pool and move on to another
                  // connection if needed
                  config.setValidationTimeout(TimeUnit.SECONDS.toMillis(1));
                  config.setMaxLifetime(TimeUnit.DAYS.toMillis(1));
                  return config;
                },
                null))
        .withDialect(new AuroraPgDialect())
        .buildAndSet();
  }
}
