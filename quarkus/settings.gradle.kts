pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id("io.quarkus") version "3.0.0.Final"
    id("com.diffplug.spotless") version "6.18.0"
    id("org.kordamp.gradle.jandex") version "1.1.0"
  }
}
rootProject.name = "quarkus"
