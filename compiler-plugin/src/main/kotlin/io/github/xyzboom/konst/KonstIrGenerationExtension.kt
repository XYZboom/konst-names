package io.github.xyzboom.konst

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class KonstIrGenerationExtension(
    private val konstAnnotationFqNames: List<String>
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val transformer = KonstTransformer(pluginContext, konstAnnotationFqNames)
        for (file in moduleFragment.files) {
            transformer.visitFile(file)
        }
    }
}