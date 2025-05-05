import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    alias(libs.plugins.vanniktech.maven.publish)
}

val myGroup = "io.github.xyzboom"
val myId = "konst-compiler-plugin"
val myVersion = "0.3.0"
group = myGroup
version = myVersion

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.kotlin.compiler)
    testImplementation(kotlin("test"))
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

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}