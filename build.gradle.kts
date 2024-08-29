plugins {
    id("java-library")
    alias(libs.plugins.intellij)
    alias(libs.plugins.kotlin.jvm)
}

group = "com.plugin"
version = "1.3"

repositories {
    mavenCentral()
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3.4")
    type.set("IC") // Target IDE Platform
    updateSinceUntilBuild.set(false)

    plugins.set(listOf(/* Plugin Dependencies */))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    downloadRobotServerPlugin {
        version.set(libs.versions.remote.robot)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Test>("unitTest") {
    useJUnitPlatform()
    filter {
        excludeTestsMatching("*IntegrationTest")
    }
}

tasks.register<Test>("integrationTest") {
    useJUnitPlatform()
    filter {
        includeTestsMatching("*IntegrationTest")
    }
}

dependencies {
    api(libs.okhttp3)
    implementation(libs.kotlin.stdlib)
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(libs.bundles.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.assertj)
    testImplementation(libs.bundles.remote.robot)
    testRuntimeOnly(libs.junit.jupiter.engine)
}