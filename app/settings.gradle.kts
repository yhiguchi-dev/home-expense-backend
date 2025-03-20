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
include("retry")
include("json-validation")
include("json-validation-jackson-adapter")
include("json-path")
