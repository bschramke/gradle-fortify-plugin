package com.github.bschramke.gradle.plugin.fortify.tasks

import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File

abstract class FortifyTranslateJavaTask : FortifyTask() {

    @get:Internal
    internal val javaPluginExtension by lazy { project.extensions.getByType<JavaPluginExtension>() }

    @TaskAction
    fun runScaBuild() {
        doChecks()
        val logfile = File(project.buildDir, "fortify/translation.log")
        val params = mutableListOf("sourceanalyzer","-b", fortifyBuildId.get())
        params.addAll(listOf("-source",javaPluginExtension.sourceCompatibility.toString()))
        params.addAll(listOf("-logfile", logfile.relativeTo(project.projectDir).path))

        val compileClasspath = project.configurations.getByName("compileClasspath").asPath
        if(!compileClasspath.isNullOrBlank()) {
            params.add("-cp")
            params.add(compileClasspath)
        }

        val sourceSet = javaPluginExtension.sourceSets.getByName("main")
        val sourcePath = sourceSet.allSource.sourceDirectories.asPath
        if(!sourcePath.isNullOrBlank()) {
            params.add("-sourcepath")
            params.add(sourcePath)
        }

        sourceSet.allSource.files.forEach { srcFile ->
            params.add(srcFile.relativeTo(project.projectDir).path)
        }

        exec(params)
    }
}