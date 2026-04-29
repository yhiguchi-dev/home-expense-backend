package dev.higuchi;

import java.util.List;
import java.util.Set;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.CleanResult;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBMigrationApp {
  private static final Logger log = LoggerFactory.getLogger(DBMigrationApp.class);
  private static final Set<String> KNOWN_COMMANDS = Set.of("migrate", "validate", "clean", "info");

  public static void main(String[] args) {
    if (args.length == 0) {
      printUsage();
      System.exit(2);
    }
    List<String> commands = List.of(args);
    for (String c : commands) {
      if (!KNOWN_COMMANDS.contains(c)) {
        log.error("Unknown command: {}", c);
        printUsage();
        System.exit(2);
      }
    }

    try {
      Flyway flyway =
          Flyway.configure().loadDefaultConfigurationFiles().envVars().loggers("slf4j").load();
      for (String cmd : commands) {
        execute(flyway, cmd);
      }
    } catch (Exception e) {
      log.error("Command failed: {}", e.getMessage(), e);
      System.exit(1);
    }
  }

  private static void execute(Flyway flyway, String cmd) {
    switch (cmd) {
      case "migrate" -> {
        MigrateResult result = flyway.migrate();
        log.atInfo()
            .setMessage("Migrate complete")
            .addKeyValue("flyway.schema", result.schemaName)
            .addKeyValue("flyway.executed", result.migrationsExecuted)
            .addKeyValue("flyway.initial_version", result.initialSchemaVersion)
            .addKeyValue("flyway.target_version", result.targetSchemaVersion)
            .addKeyValue("flyway.success", result.success)
            .addKeyValue("flyway.warnings", result.warnings.size())
            .log();
      }
      case "validate" -> flyway.validate();
      case "clean" -> {
        log.warn("Cleaning database...");
        CleanResult result = flyway.clean();
        log.atInfo()
            .setMessage("Clean complete")
            .addKeyValue("flyway.schemas_cleaned", result.schemasCleaned)
            .addKeyValue("flyway.schemas_dropped", result.schemasDropped)
            .log();
      }
      case "info" -> {
        for (var info : flyway.info().all()) {
          log.atInfo()
              .setMessage("Migration")
              .addKeyValue("flyway.script", info.getScript())
              .addKeyValue(
                  "flyway.version", info.getVersion() != null ? info.getVersion().toString() : null)
              .addKeyValue("flyway.description", info.getDescription())
              .addKeyValue("flyway.state", info.getState().name())
              .addKeyValue(
                  "flyway.installed_on",
                  info.getInstalledOn() != null
                      ? info.getInstalledOn().toInstant().toString()
                      : null)
              .addKeyValue("flyway.execution_time_ms", info.getExecutionTime())
              .log();
        }
      }
      default -> throw new IllegalStateException("Unhandled command: " + cmd);
    }
  }

  private static void printUsage() {
    System.err.println("Usage: db-migration <command> [<command>...]");
    System.err.println("Commands: migrate | validate | clean | info");
  }
}
