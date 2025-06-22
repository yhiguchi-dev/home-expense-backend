plugins {
  java
  id("com.gradleup.shadow") version "8.3.6"
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.flywaydb:flyway-database-postgresql:11.9.1")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("net.logstash.logback:logstash-logback-encoder:8.1")
  implementation("org.postgresql:postgresql:42.7.2")
}

tasks {
  jar {
    manifest {
      attributes(
        "Main-Class" to "org.example.Main",
      )
    }
  }
  shadowJar {
    mergeServiceFiles()
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
