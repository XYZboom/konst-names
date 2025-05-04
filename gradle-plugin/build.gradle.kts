import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
    `maven-publish`
}

group = "io.github.xyzboom"
version = "0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("KonstPlugin") {
            id = "io.github.xyzboom.konst"
            displayName = "Konst compiler plugin"
            description = displayName
            implementationClass = "io.github.xyzboom.konst.gradle.plugin.KonstGradleSubplugin"
        }
    }
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin.api)
    compileOnly(libs.kotlin.gradle.plugin)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}