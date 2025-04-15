plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    `maven-publish`
}

group = "com.github.xyzboom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("konst-names-source") {
            groupId = "com.github.xyzboom"
            artifactId = "konst-names"
            version = "1.0-SNAPSHOT"

            // 配置要上传的源码
            artifact(tasks.register<Jar>("sourcesJar") {
                from(sourceSets.main.get().allSource)
                archiveClassifier.set("sources")
            }) {
                classifier = "sources"
            }
        }
    }
}

dependencies {
    implementation(libs.ksp)
    ksp("com.github.xyzboom:konst-names:1.0-SNAPSHOT")
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