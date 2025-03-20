plugins {
  java
}

group = "dev.yhiguchi.home_expense"
version = "1.1.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":core"))
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
}

tasks.test {
  useJUnitPlatform()
}
