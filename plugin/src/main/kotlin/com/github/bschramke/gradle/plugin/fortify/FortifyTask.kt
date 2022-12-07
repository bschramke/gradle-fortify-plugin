package com.github.bschramke.gradle.plugin.fortify

import com.github.bschramke.gradle.plugin.fortify.util.fail
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.jvm.Throws

abstract class FortifyTask : DefaultTask() {

    init {
        group = "Fortify"
        description = "Security analysis by HP Fortify"
    }

    @get:Input
    abstract val fortifyBuildId:Property<String>

    @TaskAction
    fun fortify() {
        logger.lifecycle("Fortify BuildID => ${fortifyBuildId.get()}")
        doChecks()

        exec(listOf("sourceanalyzer","-b", fortifyBuildId.get(), "-clean"))

        val translateCommand = assembleTranslateCommand()
        exec(translateCommand)

        val fortifyBuildFolder = File(project.buildDir,"fortify")
        fortifyBuildFolder.mkdirs()
        val fortifyArtifactFileName = "${fortifyBuildId.get()}@${project.version}.mbs"
        val fortifyArtifact = File(fortifyBuildFolder, fortifyArtifactFileName)
        fortifyArtifact.createNewFile()

        exec(listOf("sourceanalyzer","-b", fortifyBuildId.get(), "-build-label", "${project.version}", "-export-build-session", fortifyArtifact.absolutePath))

        val fortifyResult = File(fortifyBuildFolder, "${project.name}-${project.version}.fpr")
        fortifyResult.createNewFile()
        exec(listOf("sourceanalyzer","-b", fortifyBuildId.get(), "-scan", "-f", fortifyResult.absolutePath))
    }

    private fun exec(params:List<String>):String {
        logger.info("[Fortify] ${params.joinToString(" ")}")

        val stdOut = ByteArrayOutputStream()

        project.exec {
            commandLine(params)
            standardOutput = stdOut
        }

        return stdOut.toString("UTF-8")
    }
    @Throws(StopExecutionException::class)
    private fun doChecks() {
        if(!fortifyBuildId.isPresent or fortifyBuildId.get().isBlank()) {
            fail("[Fortify] Mandatory parameter fortifyBuildID has not been configured.")
        }
    }

    private fun assembleTranslateCommand():List<String> {
        var translateCommand = listOf("sourceanalyzer", "-b", fortifyBuildId.get(), "-source", "1.8")

        translateCommand = addClasspathParameter(translateCommand)
        translateCommand = translateCommand.toMutableList().apply {
            add("src/**/*.java")
        }

        translateCommand = addExcludeParameter(translateCommand)

        return translateCommand
    }
    private fun addClasspathParameter(translateCommand:List<String>):List<String> {
        val result = translateCommand.toMutableList()
        val classpath = project.configurations.getByName("compileClasspath").asPath

        if(classpath.isBlank()) {
            result.addAll(listOf("-cp", classpath))
        }

        return result
    }
    private fun addExcludeParameter(translateCommand:List<String>):List<String> {
        val result = translateCommand.toMutableList()
        project.subprojects.forEach {
            val javaSourceSets = it.extensions.getByType<JavaPluginExtension>().sourceSets
            if(!javaSourceSets.getByName("test").java.isEmpty) {
                result.addAll(listOf("-exclude", "src/test/**/*.java"))
            }
        }

        return result
    }
}