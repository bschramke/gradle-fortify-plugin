/*
 * Copyright (c) 2017, Vít Kotačka
 * Copyright (c) 2022, Björn Schramke
 *
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.github.bschramke.gradle.plugin.fortify

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class FortifyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val fortifyExtension = project.extensions.create<FortifyExtension>("fortify")

        project.tasks.register<FortifyTask>("fortify") {
            fortifyBuildId.set(fortifyExtension.fortifyBuildID)
        }
    }
}