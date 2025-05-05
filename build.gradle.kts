import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

allprojects {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
}