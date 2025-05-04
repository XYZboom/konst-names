/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package io.github.xyzboom.konst.gradle.plugin.model.builder

import io.github.xyzboom.konst.gradle.plugin.KonstExtension
import io.github.xyzboom.konst.gradle.plugin.model.Konst
import org.gradle.api.Project
import org.gradle.tooling.provider.model.ToolingModelBuilder
import io.github.xyzboom.konst.gradle.plugin.model.impl.KonstImpl
import org.jetbrains.kotlin.gradle.model.AllOpen

/**
 * [ToolingModelBuilder] for [AllOpen] models.
 * This model builder is registered for Kotlin All Open sub-plugin.
 */
class KonstModelBuilder : ToolingModelBuilder {

    override fun canBuild(modelName: String): Boolean {
        return modelName == Konst::class.java.name
    }

    override fun buildAll(modelName: String, project: Project): Any {
        require(canBuild(modelName)) { "buildAll(\"$modelName\") has been called while canBeBuild is false" }
        val extension = project.extensions.getByType(KonstExtension::class.java)
        return KonstImpl(project.name, extension.myAnnotations)
    }
}
