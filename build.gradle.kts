val jUnitPlatformVersion by extra { "1.10.1" }
val jUnitJupiterVersion by extra { "5.10.1" }
val mockitoVersion by extra { "5.7.0" }
val assertJVersion by extra { "3.24.2" }

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3.4")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    testImplementation("org.junit.platform:junit-platform-launcher:$jUnitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
}
