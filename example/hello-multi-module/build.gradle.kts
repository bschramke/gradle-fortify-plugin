plugins {
    id("io.github.fortify-gradle") version "0.2.0-SNAPSHOT"
}

group = "com.github.bschramke.hello"
version = "0.1.0-SNAPSHOT"

fortify {
    fortifyBuildID = "my-kotlin-proj"
}

repositories {
    mavenCentral()
}
