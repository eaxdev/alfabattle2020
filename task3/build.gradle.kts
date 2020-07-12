import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

repositories {
    mavenCentral()
    jcenter()
}
plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.openapi.generator")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.26.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.26.1")
    implementation("org.apache.commons:commons-math3:3.6.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:junit-jupiter:1.14.3")
    testImplementation("org.testcontainers:postgresql:1.14.3")
    testImplementation("com.github.database-rider:rider-spring:1.15.0")
}

val generateResponse by tasks.registering(GenerateTask::class) {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/api.json")
    outputDir.set("$buildDir/generated-sources/task3_response")
    modelPackage.set("io.github.eaxdev.dto.response")
    generateModelDocumentation.set(false)
    generateApiDocumentation.set(false)
    configOptions.set(
        mapOf(
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson"
        )
    )
}