plugins {
  java
}

group = "dev.higuchi.homeexpense"
version = "unspecified"

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
