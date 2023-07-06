pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id("io.quarkus") version "3.2.0.Final"
    id("com.diffplug.spotless") version "6.18.0"
  }
}
rootProject.name = "quarkus"
