import org.jreleaser.gradle.plugin.tasks.JReleaserDeployTask
import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dokka)
    alias(libs.plugins.jreleaser)
    `maven-publish`
    signing
}

val myGroup = "io.github.xyzboom"
val myId = "konst-names"
val myVersion = "0.1.1"
group = myGroup
version = myVersion

repositories {
    mavenCentral()
}

val sourceJar = tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

val javadocJar = tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val localJReleaserName = "LocalForJReleaser"
val mavenSnapshotName = "MavenSnapshot"

publishing {
    repositories {
        maven {
            name = localJReleaserName
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
        maven {
            name = mavenSnapshotName
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            credentials {
                val userKey = "JRELEASER_MAVENCENTRAL_USERNAME"
                val pwdKey = "JRELEASER_MAVENCENTRAL_PASSWORD"
                username = System.getenv(userKey) ?: project.properties[userKey].toString()
                password = System.getenv(pwdKey) ?: project.properties[pwdKey].toString()
            }
        }
    }
    publications {
        create<MavenPublication>(myId) {
            groupId = myGroup
            artifactId = myId
            version = myVersion

            from(components["java"])
            artifact(sourceJar) {
                classifier = "sources"
            }
            artifact(javadocJar) {
                classifier = "javadoc"
            }

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
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications[myId])
}

jreleaser {
    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    snapshotSupported = myVersion.endsWith("-SNAPSHOT")
                    applyMavenCentralRules = true
                    sign = false // already signed by signing plugin
                    active = Active.ALWAYS
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.path)
                }
            }
        }
    }
}

val jDeploy = tasks.withType<JReleaserDeployTask> {
    val taskName = "publish" +
            myId.replaceFirstChar { it.uppercaseChar() } +
            "PublicationTo${localJReleaserName}Repository"
    dependsOn(tasks.named(taskName))
}

val cleanStagingDeploy = tasks.register("cleanStagingDeploy", Delete::class) {
    delete(layout.buildDirectory.dir("staging-deploy"))
}

tasks.register("publishKonst") {
    dependsOn(cleanStagingDeploy)
    if (!myVersion.endsWith("-SNAPSHOT")) {
        dependsOn(jDeploy)
    } else {
        val taskName = "publish" +
                myId.replaceFirstChar { it.uppercaseChar() } +
                "PublicationTo${mavenSnapshotName}Repository"
        dependsOn(tasks.named(taskName))
    }
}

dependencies {
    implementation(libs.ksp)
    kspTest(rootProject)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}