package com.github.bschramke.gradle.plugin.fortify.tasks

import com.github.bschramke.gradle.plugin.fortify.util.fail
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.StopExecutionException
import java.io.ByteArrayOutputStream
import kotlin.jvm.Throws

abstract class FortifyTask : DefaultTask() {
    init {
        group = "Fortify"
        description = "Security analysis by HP Fortify"
    }

    @get:Input
    abstract val fortifyBuildId: Property<String>

    protected fun exec(params:List<String>):String {
        logger.lifecycle("[Fortify] ${params.joinToString(" ")}")

        val stdOut = ByteArrayOutputStream()

        project.exec {
            commandLine(params)
            standardOutput = stdOut
        }

        return stdOut.toString("UTF-8")
    }

    @Throws(StopExecutionException::class)
    protected fun doChecks() {
        if(!fortifyBuildId.isPresent or fortifyBuildId.get().isBlank()) {
            fail("[Fortify] Mandatory parameter fortifyBuildID has not been configured.")
        }

        logger.lifecycle("Fortify BuildID => ${fortifyBuildId.get()}")
    }
}