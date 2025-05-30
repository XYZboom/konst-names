import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.vanniktech.maven.publish)
}

val myGroup = "io.github.xyzboom"
val myId = "konst-names-api"
val myVersion = "0.3-SNAPSHOT"
group = myGroup
version = myVersion

repositories {
    mavenCentral()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(myGroup, myId, myVersion)

    pom {
        name.set(myId)
        description.set("A Kotlin compiler plugin for generating const names.")
        url.set("https://github.com/XYZboom/konst-names")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("XYZboom")
                name.set("Xiaotian Ma")
                email.set("xyzboom@qq.com")
            }
        }

        scm {
            connection = "scm:git:https://github.com/XYZboom/konst-names.git"
            developerConnection = "scm:git:https://github.com/XYZboom/konst-names.git"
            url = "https://github.com/XYZboom/konst-names.git"
        }
    }
}

kotlin {
    jvmToolchain(8)
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    js {
        browser { }
        nodejs { }
    }

    run { // Kotlin Native
        macosX64()
        macosArm64()
        iosSimulatorArm64()
        iosX64()
        iosArm64()
        // tier 2
        linuxX64()
        linuxArm64()
        watchosSimulatorArm64()
        watchosX64()
        watchosArm32()
        watchosArm64()
        tvosSimulatorArm64()
        tvosX64()
        tvosArm64()
        // tier 3
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX86()
        androidNativeX64()
        mingwX64()
        watchosDeviceArm64()
    }
}
