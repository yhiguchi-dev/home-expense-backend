plugins {
  java
  alias(libs.plugins.boot)
  alias(libs.plugins.dependency.management)
}

group = "dev.higuchi.homeexpense"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

configurations.implementation {
  exclude(group = "commons-logging", module = "commons-logging")
}

dependencies {
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.web)
  implementation(libs.mybatis.spring.boot.starter)
  implementation(libs.mybatis)
  implementation(libs.aws.advanced.jdbc.wrapper)
  implementation("org.crac:crac")
  runtimeOnly(libs.postgresql)
  implementation(project(":core"))
  implementation(project(":network"))
  implementation(project(":postgresql-mybatis-adapter"))
//  implementation(project(":retry"))
//  implementation(project(":json-validation"))
  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.mybatis.spring.boot.starter.test)
  testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.register("printVersion") {
  doFirst {
    println(version)
  }
}
