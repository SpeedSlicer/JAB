plugins {
    id("java")
    id("application")
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.07.22-26.2")
    implementation("com.google.code.gson:gson:2.14.0")
}

application {
    mainClass.set("dev.speedslicer.main.Main")
}