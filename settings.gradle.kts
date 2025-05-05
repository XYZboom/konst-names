pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name = "konst-names"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
        }
    }
}
include("gradle-plugin")
include("compiler-plugin")
include("konst-names-api")