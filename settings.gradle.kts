// always good to nail down the root project name, because
// the root directory name may be different in some envs (e.g. CI)
// hence the following even makes sense for single-project builds
rootProject.name = "gradle-fortify-plugin"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.7.22"
    }
    repositories {
        gradlePluginPortal()
    }
}

includeBuild("plugin")
includeBuild("example/hello-java")
includeBuild("example/hello-kotlin")
includeBuild("example/hello-multi-module")
includeBuild("example/hello-android")