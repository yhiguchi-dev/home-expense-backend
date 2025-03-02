plugins {
  id("java")
}

group = "dev.higuchi.homeexpense"
version = "1.1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
}

tasks.test {
  useJUnitPlatform()
}
