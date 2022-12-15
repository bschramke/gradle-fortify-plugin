/*
 * Copyright (c) 2017, Vít Kotačka
 * Copyright (c) 2022, Björn Schramke
 *
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.github.bschramke.gradle.plugin.fortify

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.gradle.internal.tasks.factory.registerTask
import com.github.bschramke.gradle.plugin.fortify.tasks.FortifyCleanTask
import com.github.bschramke.gradle.plugin.fortify.tasks.FortifyTranslateAndroidVariantTask
import com.github.bschramke.gradle.plugin.fortify.tasks.FortifyTranslateJavaTask
import com.github.bschramke.gradle.plugin.fortify.util.isAndroidModule
import com.github.bschramke.gradle.plugin.fortify.util.isJavaModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class FortifyPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        ensureTargetIsRootProject(project)

        project.logger.lifecycle("num childs => ${project.childProjects.size}")

        val fortifyExtension = project.extensions.create<FortifyExtension>("fortify")

        project.allprojects {
            afterEvaluate {
                logger.lifecycle("configure ${this.name}")
                if(this.isJavaModule())
                    configureJavaProject(this, fortifyExtension)
                else if(this.isAndroidModule()) {
                    configureAndroidProject(this, fortifyExtension)
                }
            }
        }

        project.tasks.apply {
            register<FortifyAnalysisTask>("fortify") {
                this.fortifyBuildId.set(fortifyExtension.fortifyBuildID ?: project.rootProject.name)
            }

            register<FortifyCleanTask>("fortifyClean") {
                this.fortifyBuildId.set(fortifyExtension.fortifyBuildID ?: project.rootProject.name)
            }
        }

    }
    private fun configureJavaProject(target: Project, fortifyExtension: FortifyExtension) {
        target.logger.lifecycle("Configure ${target.name} as Java Project")
        registerTranslateTasks(target, fortifyExtension)

    }

    private fun configureAndroidProject(target: Project, fortifyExtension: FortifyExtension) {
        target.logger.lifecycle("Configure ${target.name} as Android Project")
        val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant:Variant ->
            target.tasks.registerTask(FortifyTranslateAndroidVariantTask.CreateAction(variant))
        }
    }
    private fun ensureTargetIsRootProject(target:Project) {
        require(target.parent == null) { "Fortify plugin must applied to root-project only!" }
    }

    private fun registerTranslateTasks(target: Project, fortifyExtension: FortifyExtension) {
        target.afterEvaluate {
            if(target.isJavaModule()) {
                target.tasks.register<FortifyTranslateJavaTask>("fortifyTranslate") {
                    this.fortifyBuildId.set(fortifyExtension.fortifyBuildID ?: project.rootProject.name)
                }
            }
        }

        target.childProjects.values.forEach {
            registerTranslateTasks(it, fortifyExtension)
        }
    }
}