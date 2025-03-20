plugins {
  java
}

group = "dev.higuchi.homeexpense"
version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
  testImplementation(project(":json-validation-jackson-adapter"))
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
}

tasks.test {
  useJUnitPlatform()
}
