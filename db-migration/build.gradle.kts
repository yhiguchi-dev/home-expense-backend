plugins {
  application
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.flyway.database.postgresql)
  implementation(libs.logback.classic)
  implementation(libs.logstash.logback.encoder)
  implementation(libs.postgresql)
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
  doLast {
    configurations.filter { it.isCanBeResolved }.forEach { it.resolve() }
  }
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
