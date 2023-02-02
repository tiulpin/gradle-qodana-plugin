import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key)?.toString()

plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm") version "1.8.10"
    id("com.gradle.plugin-publish") version "0.19.0"
}

group = "org.jetbrains.qodana"
version = "0.1." + properties("buildNumber")

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    test {
        configureTests(this)
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
        distributionUrl = "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
    }
}

fun configureTests(testTask: Test) {
    val testGradleHomePath = "$buildDir/testGradleHome"
    testTask.doFirst {
        File(testGradleHomePath).mkdir()
    }
    testTask.systemProperties["test.gradle.home"] = testGradleHomePath
    testTask.systemProperties["test.gradle.default"] = properties("gradleVersion")
    testTask.systemProperties["test.gradle.version"] = properties("testGradleVersion")
    testTask.systemProperties["test.gradle.arguments"] = properties("testGradleArguments")
    testTask.outputs.dir(testGradleHomePath)
}

gradlePlugin {
    plugins.create("qodana") {
        id = "org.jetbrains.qodana"
        displayName = "Gradle Qodana Plugin"
        implementationClass = "org.jetbrains.qodana.QodanaPlugin"
    }
}

pluginBundle {
    website = "https://github.com/JetBrains/gradle-qodana-plugin"
    vcsUrl = "https://github.com/JetBrains/gradle-qodana-plugin"

    description = "Gradle Qodana Plugin allows to run and configure Idea inspections for Gradle project."
    tags = listOf("qodana", "intellij", "idea", "inspections")
}
