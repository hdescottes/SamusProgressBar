val jUnitPlatformVersion by extra { "1.10.1" }
val jUnitJupiterVersion by extra { "5.10.1" }
val mockitoVersion by extra { "5.7.0" }
val assertJVersion by extra { "3.24.2" }
val remoteRobotVersion by extra { "0.11.22" }
val okHttp3Version by extra { "4.12.0" }

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java-library")
    id("org.jetbrains.intellij") version "1.17.0"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "com.plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3.4")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
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

    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    downloadRobotServerPlugin {
        version = remoteRobotVersion
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    val pluginFileName = properties("pluginName") + "-" + version + ".jar"
    systemProperty("test.plugin.path", projectDir.resolve("build/libs/$pluginFileName").absolutePath)
}

dependencies {
    api("com.squareup.okhttp3:okhttp:${okHttp3Version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.junit.platform:junit-platform-launcher:$jUnitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("com.intellij.remoterobot:remote-robot:$remoteRobotVersion")
    testImplementation("com.intellij.remoterobot:remote-fixtures:$remoteRobotVersion")
    testImplementation("com.intellij.remoterobot:ide-launcher:$remoteRobotVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitJupiterVersion")
    implementation(kotlin("stdlib-jdk8"))
}