pluginManagement {
  val quarkusPluginVersion: String by settings
  val quarkusPluginId: String by settings
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id(quarkusPluginId) version quarkusPluginVersion
    id("com.diffplug.spotless") version "6.17.0"
  }
}
rootProject.name = "quarkus"
