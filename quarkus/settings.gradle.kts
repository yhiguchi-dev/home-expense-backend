pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id("io.quarkus") version "3.0.0.Final"
    id("com.diffplug.spotless") version "6.17.0"
  }
}
rootProject.name = "quarkus"
