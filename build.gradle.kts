plugins {
    java
    application
    id("com.gradleup.shadow") version "9.6.0"
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.07.22-26.2")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}

application {
    mainClass.set("dev.speedslicer.main.Main")
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}