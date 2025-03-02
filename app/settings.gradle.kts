pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
}
rootProject.name = "app"
include("core")
include("spring-boot")
include("quarkus")
include("postgresql-mybatis-adapter")
include("network")
