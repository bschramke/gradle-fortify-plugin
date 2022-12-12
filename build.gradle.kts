
afterEvaluate {
    gradle.includedBuilds.forEach {
        val relativePath = it.projectDir.relativeTo(rootDir).path
        if(relativePath.startsWith("example")) {
            registerExampleTasks(it)
        }
    }
}

fun normalizeModuleName(name:String) = name.split('-')
    .joinToString("") { it.capitalize() }

fun registerExampleTasks(module: IncludedBuild) {
    tasks {
        val name = normalizeModuleName(module.name)
        val buildTask = register("buildExample$name") {
            group = "Build"
            dependsOn(gradle.includedBuild(module.name).task(":build"))
        }
        val fortifyCleanTask = register("fortifyCleanExample$name") {
            group = "Fortify"
            dependsOn(gradle.includedBuild(module.name).task(":fortifyClean"))
        }
        val fortifyTranslateTask = register("fortifyTranslateExample$name") {
            group = "Fortify"
            dependsOn(gradle.includedBuild(module.name).task(":fortifyTranslateRun"))
        }
        val fortifyTask = register("fortifyExample$name") {
            group = "Fortify"
            dependsOn(gradle.includedBuild(module.name).task(":fortify"))
        }
        val printTask = register("tasksExample$name") {
            group = "Help"
            dependsOn(gradle.includedBuild(module.name).task(":tasks"))
        }
    }
}