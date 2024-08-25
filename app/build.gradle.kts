plugins {
  java
  alias(libs.plugins.io.quarkus)
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(enforcedPlatform(libs.quarkus.bom))
  implementation(libs.quarkus.resteasy.reactive.jackson)
  implementation(libs.quarkus.hibernate.validator)
  implementation(libs.quarkus.jdbc.postgresql)
  implementation(libs.quarkus.arc)
  implementation(libs.quarkus.resteasy.reactive)
  implementation(libs.quarkus.mybatis)
  implementation(libs.quarkus.logging.json)
  testImplementation(libs.quarkus.junit5)
}

group = "dev.yhiguchi.home_expense"

version = "1.1.0"

System.getenv("DEPLOY_ENV")?.let { deployEnv ->
  version = "$version-$deployEnv"
}

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

task("printVersion") {
  doFirst {
    println(version)
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
