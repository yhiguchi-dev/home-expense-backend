plugins {
  id("java")
}

group = "dev.higuchi.homeexpense"
version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":json-validation"))
  implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
}

tasks.test {
  useJUnitPlatform()
}
