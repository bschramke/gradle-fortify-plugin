package com.github.bschramke.gradle.plugin.fortify.tasks

import com.android.build.api.variant.Variant
import com.android.build.gradle.internal.component.ComponentCreationConfig
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.android.build.gradle.internal.tasks.NonIncrementalTask
import com.android.build.gradle.internal.tasks.factory.VariantTaskCreationAction
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.util.concurrent.Callable

@CacheableTask
abstract class FortifyTranslateAndroidVariantTask : NonIncrementalTask() {
    @get:Input
    abstract val moduleName: Property<String>

    @get:Input
    abstract val moduleVersion: Property<String>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: ConfigurableFileCollection

    @get:Classpath
    abstract val classpath: ConfigurableFileCollection
    @Override
    override fun doTaskAction() {
        logger.lifecycle("Hello from Translate Job")

        logger.lifecycle("moduleName => ${moduleName.get()}")
        logger.lifecycle("moduleVersion => ${moduleVersion.get()}")
        logger.lifecycle("sources => ${sources.asPath}")
        logger.lifecycle("classpath => ${classpath.asPath}")
    }

    class CreateAction(
        creationConfig: Variant
    ): VariantTaskCreationAction<FortifyTranslateAndroidVariantTask, ComponentCreationConfig>(
        creationConfig as ComponentCreationConfig
    ) {
        override val name: String
            get() = computeTaskName("fortifyTranslate")
        override val type: Class<FortifyTranslateAndroidVariantTask>
            get() = FortifyTranslateAndroidVariantTask::class.java

        override fun configure(task: FortifyTranslateAndroidVariantTask) {
            super.configure(task)

            task.moduleName.set(task.project.name)
            task.moduleVersion.set(task.project.version.toString())

            task.sources.from(
                Callable { creationConfig.variantSources.getSourceFiles { it.javaDirectories } },
                Callable { creationConfig.variantSources.getSourceFiles { it.kotlinDirectories } },
            )

            task.classpath.from(
                creationConfig.getJavaClasspath(
                    AndroidArtifacts.ConsumedConfigType.COMPILE_CLASSPATH,
                    AndroidArtifacts.ArtifactType.CLASSES_JAR, null)
            )
        }
    }
}