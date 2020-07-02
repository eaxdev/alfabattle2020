import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("javax.validation:validation-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

val generateAlfaAtmClient by tasks.registering(GenerateTask::class) {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/Сервис проверки статуса банкоматов.yaml")
    outputDir.set("$buildDir/generated-sources/atm_alfa_api")
    modelPackage.set("io.github.eaxdev.dto.atmapi")
    generateModelDocumentation.set(false)
    generateApiDocumentation.set(false)
    configOptions.set(
        mapOf(
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson"
        )
    )
}

val generateResponse by tasks.registering(GenerateTask::class) {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/src/main/resources/alfa_api.json")
    outputDir.set("$buildDir/generated-sources/task1_response")
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