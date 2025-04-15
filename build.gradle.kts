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
val myVersion = "0.1.0"
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

tasks.withType<JReleaserDeployTask> {
    dependsOn(tasks.publish)
}

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
    publications {
        create<MavenPublication>(myId) {
            from(components["java"])

            groupId = myGroup
            artifactId = myId
            version = myVersion

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
    sign(publishing.publications)
}

jreleaser {
    deploy {
        maven {
            mavenCentral {
                this.register("sonatype") {
                    deploymentId = "fb6387b4-e812-4c5b-b03b-3bdb7ebfcb65"
                    applyMavenCentralRules = true
                    verifyPom = false
                    sign = false // already signed by signing plugin
                    active = Active.ALWAYS
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.path)
                }
            }
        }
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