plugins {
  java
  alias(libs.plugins.com.diffplug.spotless)
}

repositories {
  mavenCentral()
  mavenLocal()
}

spotless {
  java {
    target("**/src/**/*.java")
    importOrder()
    removeUnusedImports()
    googleJavaFormat()
  }
  kotlinGradle {
    target("**/*.kts")
    ktlint().editorConfigOverride(mapOf("indent_size" to 2))
  }
}
