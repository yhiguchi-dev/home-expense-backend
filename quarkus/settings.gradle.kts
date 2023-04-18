pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id("io.quarkus") version "2.16.6.Final"
    id("com.diffplug.spotless") version "6.18.0"
  }
}
rootProject.name = "quarkus"
