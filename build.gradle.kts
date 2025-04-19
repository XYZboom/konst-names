import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish)
}

val myGroup = "io.github.xyzboom"
val myId = "konst-names"
val myVersion = "0.2.0"
group = myGroup
version = myVersion

repositories {
    mavenCentral()
}

val localJReleaserName = "LocalForJReleaser"
val mavenSnapshotName = "MavenSnapshot"

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(myGroup, myId, myVersion)

    pom {
        name.set(myId)
        description.set("A KSP project for generating const names.")
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
    sourceSets {
        applyDefaultHierarchyTemplate()
        commonMain {

        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
        val noJsMain by creating {
            dependsOn(commonMain.get())
        }
        val noJsTest by creating {
            dependsOn(commonTest.get())
        }
        jvmMain {
            dependsOn(noJsMain)
            dependencies {
                implementation(libs.ksp)
            }
        }
        run {
            macosX64Main { dependsOn(noJsMain) }
            macosArm64Main { dependsOn(noJsMain) }
            iosSimulatorArm64Main { dependsOn(noJsMain) }
            iosX64Main { dependsOn(noJsMain) }
            iosArm64Main { dependsOn(noJsMain) }
            // tier 2
            linuxX64Main { dependsOn(noJsMain) }
            linuxArm64Main { dependsOn(noJsMain) }
            watchosSimulatorArm64Main { dependsOn(noJsMain) }
            watchosX64Main { dependsOn(noJsMain) }
            watchosArm32Main { dependsOn(noJsMain) }
            watchosArm64Main { dependsOn(noJsMain) }
            tvosSimulatorArm64Main { dependsOn(noJsMain) }
            tvosX64Main { dependsOn(noJsMain) }
            tvosArm64Main { dependsOn(noJsMain) }
            // tier 3
            androidNativeArm32Main { dependsOn(noJsMain) }
            androidNativeArm64Main { dependsOn(noJsMain) }
            androidNativeX86Main { dependsOn(noJsMain) }
            androidNativeX64Main { dependsOn(noJsMain) }
            mingwX64Main { dependsOn(noJsMain) }
            watchosDeviceArm64Main { dependsOn(noJsMain) }
        }
        
        run {
            macosX64Test { dependsOn(noJsTest) }
            macosArm64Test { dependsOn(noJsTest) }
            iosSimulatorArm64Test { dependsOn(noJsTest) }
            iosX64Test { dependsOn(noJsTest) }
            iosArm64Test { dependsOn(noJsTest) }
            // tier 2
            linuxX64Test { dependsOn(noJsTest) }
            linuxArm64Test { dependsOn(noJsTest) }
            watchosSimulatorArm64Test { dependsOn(noJsTest) }
            watchosX64Test { dependsOn(noJsTest) }
            watchosArm32Test { dependsOn(noJsTest) }
            watchosArm64Test { dependsOn(noJsTest) }
            tvosSimulatorArm64Test { dependsOn(noJsTest) }
            tvosX64Test { dependsOn(noJsTest) }
            tvosArm64Test { dependsOn(noJsTest) }
            // tier 3
            androidNativeArm32Test { dependsOn(noJsTest) }
            androidNativeArm64Test { dependsOn(noJsTest) }
            androidNativeX86Test { dependsOn(noJsTest) }
            androidNativeX64Test { dependsOn(noJsTest) }
            mingwX64Test { dependsOn(noJsTest) }
            watchosDeviceArm64Test { dependsOn(noJsTest) }
        }
    }
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

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

dependencies {
    add("kspJvmTest", rootProject)
}