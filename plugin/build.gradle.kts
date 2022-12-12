plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.1.0"
}

gradlePlugin {
    plugins {
        create("fortifyPlugin") {
            id = "com.github.fortify-gradle"
            implementationClass = "com.github.bschramke.gradle.plugin.fortify.FortifyPlugin"
            displayName = "Gradle Fortify Plugin"
            description = "A Gradle plugin for building and publishing of Fortify artifacts for a static security analysis."
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
    mavenCentral()
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

tasks.withType<org.gradle.jvm.tasks.Jar>() {
    from(projectDir) {
        include("LICENSE")
        into("META-INF")
    }
}
