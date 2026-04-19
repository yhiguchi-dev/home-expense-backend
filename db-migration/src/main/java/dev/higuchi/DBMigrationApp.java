package dev.higuchi;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBMigrationApp {
  static void main(String[] args) {
    Logger log = LoggerFactory.getLogger(DBMigrationApp.class);
    try {
      Flyway flyway = Flyway.configure().loadDefaultConfigurationFiles().loggers("slf4j").load();
      log.info("Flyway configuration loaded: {}", flyway.getConfiguration().getUrl());
      for (String arg : args) {
        switch (arg) {
          case "migrate":
            log.info("Starting migration...");
            flyway.migrate();
            break;
          case "validate":
            log.info("Validating migrations...");
            flyway.validate();
            break;
          case "clean":
            log.info("Cleaning database...");
            flyway.clean();
            break;
          case "info":
            log.info("Showing migration info...");
            for (var info : flyway.info().all()) {
              log.info(
                  "Migration: {} - Version: {} - Description: {}",
                  info.getScript(),
                  info.getVersion(),
                  info.getDescription());
            }
            break;
          default:
            throw new IllegalArgumentException("Unknown argument: " + arg);
        }
      }
    } catch (Exception e) {
      log.error("Migration failed {}", e.getMessage(), e);
      throw e;
    }
  }
}
