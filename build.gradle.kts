plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
}

group = "com.realmgate"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(uri("https://nexus.lucko.me/repository/maven-hytale/"))
}

dependencies {
    implementation("ch.jalu:configme:1.4.1")
    implementation("redis.clients:jedis:7.1.0")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.7")
    compileOnly("com.hypixel.hytale:HytaleServer:2026.01.17-4b0f30090-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.named<ProcessResources>("processResources") {
    inputs.property("version", project.version)

    filesMatching("**/manifest.json") {
        expand(
            "version" to project.version
        )
    }
    exclude("META-INF/**")

}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    relocate("com.zaxxer.hikari", "com.realmgate.hikari")
    relocate("redis.clients", "com.realmgate.libs.redis.clients")
    relocate("ch.jalu.configme", "com.realmgate.libs.configme")
}