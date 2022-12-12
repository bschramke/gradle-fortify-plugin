package com.github.bschramke.gradle.plugin.fortify.tasks

import org.gradle.api.tasks.TaskAction

abstract class FortifyCleanTask : FortifyTask() {

    @TaskAction
    fun runScaClean() {
        doChecks()
        exec(listOf("sourceanalyzer","-b", fortifyBuildId.get(), "-clean"))
    }
}