package io.github.xyzboom.konst

import io.github.xyzboom.konst.KonstCommandLineProcessor.Companion.ANNOTATION
import io.github.xyzboom.konst.diagnostics.KonstDiagnosticSuppressor
import io.github.xyzboom.konst.fir.FirKonstExtensionRegistrar
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
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

        DiagnosticSuppressor.registerExtension(KonstDiagnosticSuppressor())
        FirExtensionRegistrarAdapter.registerExtension(FirKonstExtensionRegistrar(annotations))
        IrGenerationExtension.registerExtension(KonstIrGenerationExtension(annotations))
    }
}