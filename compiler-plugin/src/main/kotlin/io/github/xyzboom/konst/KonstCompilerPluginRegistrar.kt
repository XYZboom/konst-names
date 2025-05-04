package io.github.xyzboom.konst

import io.github.xyzboom.konst.KonstCommandLineProcessor.Companion.ANNOTATION
import io.github.xyzboom.konst.diagnostics.KonstDiagnosticSuppressor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.diagnostics.DiagnosticSuppressor

@ExperimentalCompilerApi
class KonstCompilerPluginRegistrar: CompilerPluginRegistrar() {
    override val supportsK2: Boolean
        get() = true

    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration
    ) {
        val annotations = configuration.get(ANNOTATION)?.toMutableList() ?: mutableListOf()
        if (annotations.isEmpty()) return

        val messageCollector = configuration.get(
            CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE
        )

        DiagnosticSuppressor.registerExtension(KonstDiagnosticSuppressor())
        IrGenerationExtension.registerExtension(KonstIrGenerationExtension(annotations))
    }
}