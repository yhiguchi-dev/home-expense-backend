pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id("io.quarkus") version "3.4.2"
    id("com.diffplug.spotless") version "6.20.0"
  }
}
rootProject.name = "quarkus"
