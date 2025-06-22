package org.example;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static void main(String[] args) {
    Logger log = LoggerFactory.getLogger(Main.class);
    try {
      Flyway flyway = Flyway.configure().loadDefaultConfigurationFiles().loggers("slf4j").load();
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
          case "info":
            log.info("Showing migration info...");
            for (var info : flyway.info().all()) {
              log.info(
                  "Migration: {} - Version: {} - Description: {}",
                  info.getScript(),
                  info.getVersion(),
                  info.getDescription());
            }
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
