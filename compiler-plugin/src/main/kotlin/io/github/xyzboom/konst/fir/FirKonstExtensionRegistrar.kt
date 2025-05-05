package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class FirKonstExtensionRegistrar(val konstAnnotationFqNames: List<String>) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::FirKonstStatusTransformerExtension
        +FirKonstPredicateMatcher.getFactory(konstAnnotationFqNames)
    }
}