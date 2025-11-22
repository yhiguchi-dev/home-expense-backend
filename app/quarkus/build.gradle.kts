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
//  implementation(libs.quarkus.arc)
//  implementation(libs.quarkus.resteasy.reactive)
  implementation(libs.quarkus.mybatis)
  implementation(libs.mybatis)
  implementation(project(":core"))
  implementation(project(":postgresql-mybatis-adapter"))
  implementation(libs.quarkus.logging.json)
  testImplementation(libs.quarkus.junit5)
}

group = "dev.higuchi.homeexpense"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}

System.getenv("DEPLOY_ENV")?.let { deployEnv ->
  version = "$version-$deployEnv"
}

tasks.register("printVersion") {
  doFirst {
    println(version)
  }
}
