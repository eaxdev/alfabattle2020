import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
    kotlin("jvm") version "1.3.72" apply false
    kotlin("plugin.spring") version "1.3.50" apply false
    id("org.openapi.generator") version "4.3.1" apply false
}

repositories {
    mavenCentral()
}

description = "AlfaBattle 2020 contest"

subprojects {
    group = "io.github.eaxdev"
    version = "1.0"

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_11.majorVersion
            targetCompatibility = JavaVersion.VERSION_11.majorVersion
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }
        withType<Test> {
            useJUnitPlatform()
        }
    }

    repositories {
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
    }
}