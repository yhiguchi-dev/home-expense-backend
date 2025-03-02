plugins {
  id("java")
}

group = "dev.higuchi.homeexpense"
version = "1.1.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":core"))
  implementation(libs.mybatis)
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.postgresql)
}

sourceSets {
  main {
    resources.setSrcDirs(setOf("src/main/java", "src/main/resources"))
  }
  test {
    resources.setSrcDirs(setOf("src/test/java", "src/test/resources"))
  }
}

tasks.test {
  useJUnitPlatform()
}
