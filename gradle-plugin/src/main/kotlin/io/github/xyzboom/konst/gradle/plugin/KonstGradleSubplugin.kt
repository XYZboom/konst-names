package io.github.xyzboom.konst.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import io.github.xyzboom.konst.gradle.plugin.model.builder.KonstModelBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import javax.inject.Inject

class KonstGradleSubplugin
@Inject internal constructor(
    private val registry: ToolingModelBuilderRegistry
) : KotlinCompilerPluginSupportPlugin {
    companion object {
        private const val KONST_ARTIFACT_NAME = "konst-compiler-plugin"

        private const val ANNOTATION_ARG_NAME = "annotation"
    }

    override fun apply(target: Project) {
        target.extensions.create("konst", KonstExtension::class.java)
        registry.register(KonstModelBuilder())
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        val konstExtension = project.extensions.getByType(KonstExtension::class.java)

        return project.provider {
            val options = mutableListOf<SubpluginOption>()

            for (anno in konstExtension.myAnnotations) {
                options += SubpluginOption(ANNOTATION_ARG_NAME, anno)
            }

            options
        }
    }

    override fun getCompilerPluginId() = "io.github.xyzboom.konst"
    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(groupId = "io.github.xyzboom", artifactId = KONST_ARTIFACT_NAME, version = "0.2.0")
}