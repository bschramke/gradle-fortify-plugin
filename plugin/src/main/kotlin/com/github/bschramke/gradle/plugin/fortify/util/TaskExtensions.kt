package com.github.bschramke.gradle.plugin.fortify.util

import org.gradle.api.Task
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskExecutionException

fun Task.fail(message:String):Nothing = throw TaskExecutionException(this, StopExecutionException(message))