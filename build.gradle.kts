plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.realmgate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.jalu:configme:1.4.1")
    compileOnly(files("libs/HytaleServer.jar"))
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}


tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("**/manifest.json") {
            expand(
                "version" to project.version,
            )
        }
    }

    shadowJar {
        archiveClassifier.set("")
    }
}