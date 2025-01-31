plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "io.github.fortify-gradle"
version = "0.2.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("fortifyPlugin") {
            id = "io.github.fortify-gradle"
            implementationClass = "com.github.bschramke.gradle.plugin.fortify.FortifyPlugin"
            displayName = "Gradle Fortify Plugin"
            description =
                "A Gradle plugin for building and publishing of Fortify artifacts for a static security analysis."
        }
    }
}

pluginBundle {
    website = "https://github.com/bschramke/gradle-fortify-plugin"
    vcsUrl = "https://github.com/bschramke/gradle-fortify-plugin"
    description = "A Gradle plugin for building and publishing of Fortify artifacts for a static security analysis."
    tags = listOf("fortify", "quality", "qa", "analysis", "security")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly("com.android.tools.build:gradle-api:7.1.3")
    compileOnly("com.android.tools.build:gradle:7.1.3")
//    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin")
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<org.gradle.jvm.tasks.Jar>() {
    from(projectDir) {
        include("LICENSE")
        into("META-INF")
    }
}
