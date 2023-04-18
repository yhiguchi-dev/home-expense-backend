plugins {
  java
  id("io.quarkus")
  id("com.diffplug.spotless")
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:2.16.6.Final"))
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
  implementation("io.quarkus:quarkus-hibernate-validator")
  implementation("io.quarkus:quarkus-jdbc-postgresql")
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-resteasy-reactive")
  implementation("io.quarkiverse.mybatis:quarkus-mybatis:1.0.7")
  implementation("io.quarkus:quarkus-logging-json")
  testImplementation("io.quarkus:quarkus-junit5")
}

group = "dev.yhiguchi.home_expense"
version = "1.0.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
  main {
    resources.setSrcDirs(setOf("src/main/java", "src/main/resources"))
  }
  test {
    resources.setSrcDirs(setOf("src/test/java", "src/test/resources"))
  }
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
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
