
tasks.register("buildExampleHelloJava") {
    group = "Build"
    dependsOn(gradle.includedBuild("hello-java").task(":build"))
}

tasks.register("buildExampleHelloKotlin") {
    group = "Build"
    dependsOn(gradle.includedBuild("hello-kotlin").task(":build"))
}

tasks.register("fortifyExampleHelloJava") {
    group = "Fortify"
    dependsOn(gradle.includedBuild("hello-java").task(":fortify"))
}

tasks.register("fortifyExampleHelloKotlin") {
    group = "Fortify"
    dependsOn(gradle.includedBuild("hello-kotlin").task(":fortify"))
}