plugins {
  application
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.flyway.core)
  implementation(libs.slf4j.api)

  runtimeOnly(libs.flyway.database.postgresql)
  runtimeOnly(libs.flyway.mysql)
  runtimeOnly(libs.postgresql)
  runtimeOnly(libs.mysql.connector.j)

  runtimeOnly(libs.logback.classic)
  runtimeOnly(libs.logstash.logback.encoder)
}

application {
  mainClass.set("dev.higuchi.DBMigrationApp")
  applicationName = "db-migration"
}

java {
  sourceCompatibility = JavaVersion.VERSION_25
  targetCompatibility = JavaVersion.VERSION_25
}

tasks.register("resolveDependencies") {
  description = "Pre-resolves all resolvable configurations so their artifacts are cached."

  val resolvable = configurations.matching { it.isCanBeResolved }
  inputs.files(resolvable.map { it.incoming.files })

  doLast { logger.lifecycle("Resolved {} dependency files", inputs.files.files.size) }
}

spotless {
  java {
    target("**/src/**/*.java")
    importOrder()
    removeUnusedImports()
    googleJavaFormat()
  }
  kotlinGradle {
    target("**/*.kts")
    ktlint().editorConfigOverride(mapOf("indent_size" to 2))
  }
}
