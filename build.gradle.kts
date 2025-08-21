import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    alias(libs.plugins.intellij.platform)
    alias(libs.plugins.kotlin.jvm)
}

group = "com.plugin"
version = "1.4"

repositories {
    mavenCentral()
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            untilBuild = provider { null }
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
/*
    test {
        description = "Runs unit tests"

        // Exclure tous les tests se terminant par IntegrationTest
        filter {
            excludeTestsMatching("*IntegrationTest")
        }
    }

    register<Test>("integrationTests") {
        description = "Runs integration tests with RemoteRobot"
        group = "verification"

        testClassesDirs = sourceSets["test"].output.classesDirs
        classpath = sourceSets["test"].runtimeClasspath

        filter {
            includeTestsMatching("*IntegrationTest")
        }
    }*/

    buildSearchableOptions {
        enabled = false
    }
}

intellijPlatformTesting {
    runIde {
        create("runIdeForUiTests") {
            task {
                description = "Runs integration tests"

                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                        "-Dide.mac.file.chooser.native=false",
                        "-DjbScreenMenuBar.enabled=false",
                        "-Dapple.laf.useScreenMenuBar=false",
                        "-Didea.trust.all.projects=true",
                        "-Dide.show.tips.on.startup.default.value=false"
                    )
                }
            }
            plugins {
                robotServerPlugin(libs.versions.remote.robot)
            }
        }
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
    testRuntimeOnly("junit:junit:4.13.2")
    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        intellijIdeaCommunity(providers.gradleProperty("platformVersion"))
    }
}