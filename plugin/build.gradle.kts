plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("groovy")
    id("com.gradle.plugin-publish") version "0.9.7"
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

repositories {
    mavenCentral()
}

dependencies {
    implementation(localGroovy())
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

// jar {
//     from(projectDir) {
//         include("LICENSE")
//         into("META-INF")
//     }
// }

// pluginBundle {
//     website = "https://github.com/sw-samuraj/gradle-fortify-plugin"
//     vcsUrl = "https://github.com/sw-samuraj/gradle-fortify-plugin"
//     description = "Gradle plugin for running  Fortify static code analysis."
//     tags = listOf("fortify", "quality", "qa", "analysis", "security")
//
//     plugins {
//         create("fortifyPlugin") {
//             id = "cz.swsamuraj.fortify"
//             implementationClass = "cz.swsamuraj.gradle.fortify.FortifyPlugin"
//             displayName = "Gradle Fortify Plugin"
//             description = "A Gradle plugin for building and publishing of Fortify artifacts for a static security analysis."
//         }
//     }
// }
