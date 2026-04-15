plugins {
  java
  jacoco
  alias(libs.plugins.io.quarkus)
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(enforcedPlatform(libs.quarkus.bom))
  implementation(libs.quarkus.rest.jackson)
  implementation(libs.quarkus.hibernate.validator)
  implementation(libs.quarkus.jdbc.postgresql)
  implementation(libs.quarkus.logging.json)
  implementation(libs.quarkus.opentelemetry)
  testImplementation(libs.quarkus.jacoco)
  testImplementation(libs.quarkus.junit5)
  testImplementation(libs.rest.assured)
}

group = "dev.yhiguchi.home_expense"

version = "2.0.0"

System.getenv("DEPLOY_ENV")?.let { deployEnv ->
  version = "$version-$deployEnv"
}

java {
  sourceCompatibility = JavaVersion.VERSION_25
  targetCompatibility = JavaVersion.VERSION_25
}

val integrationTest by sourceSets.getting {
  compileClasspath += sourceSets.test.get().output
  runtimeClasspath += sourceSets.test.get().output
}

configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
  finalizedBy(tasks.jacocoTestReport)
  configure<JacocoTaskExtension> {
    excludeClassLoaders = listOf("*QuarkusClassLoader")
    destinationFile = layout.buildDirectory.file("jacoco-quarkus.exec").get().asFile
  }
}

tasks.jacocoTestReport {
  executionData.setFrom(layout.buildDirectory.file("jacoco-quarkus.exec"))
  reports {
    xml.required.set(true)
    html.required.set(true)
    csv.required.set(true)
  }
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}

tasks.register("printVersion") {
  description = "Prints the project version."
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
