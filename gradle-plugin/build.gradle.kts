import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    alias(libs.plugins.vanniktech.maven.publish)
}

group = "io.github.xyzboom"
version = "0.3-SNAPSHOT"
val myGroup = "io.github.xyzboom.konst"
val myId = "io.github.xyzboom.konst.gradle.plugin"
// remember to change version in Kotlin class
val myVersion = "0.3.0"

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