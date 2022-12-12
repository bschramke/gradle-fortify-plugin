package com.github.bschramke.gradle.plugin.fortify.util

import org.gradle.api.Project

fun Project.isJavaModule():Boolean = this.plugins.hasPlugin("java")
fun Project.isAndroidAppModule():Boolean = this.plugins.hasPlugin("com.android.application")
fun Project.isAndroidLibModule():Boolean = this.plugins.hasPlugin("com.android.library")
fun Project.isAndroidModule():Boolean = isAndroidAppModule() or isAndroidLibModule()
fun Project.isRootProject():Boolean = this.parent == null