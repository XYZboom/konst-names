package io.github.xyzboom.konst

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrFail

class KonstTransformer(
    private val context: IrPluginContext,
    private val konstAnnotationFqNames: List<String>,
) : IrElementTransformerVoidWithContext() {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (!declaration.hasKonstAnno(konstAnnotationFqNames)) {
            return super.visitPropertyNew(declaration)
        }
        return super.visitPropertyNew(declaration)
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitCall(expression: IrCall): IrExpression {
        val property = currentProperty
        if (property == null) {
            return super.visitCall(expression)
        }
        if (expression.isKClassSimpleNameCall() || expression.isKClassQualifiedNameCall()) {
            if (expression.arguments.size != 1) {
                context.messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Expect one arguments here.",
                    expression.getCompilerMessageLocation(currentFile)
                )
                return super.visitCall(expression)
            }
            val arg = expression.arguments.single()
            if (arg !is IrClassReference) {
                context.messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Only compile time constant class reference can be evaluated.",
                    arg?.getCompilerMessageLocation(currentFile) ?: expression.getCompilerMessageLocation(currentFile)
                )
                return super.visitCall(expression)
            }
            val constValue = if (expression.isKClassSimpleNameCall()) {
                arg.classType.classOrFail.owner.name.asString()
            } else {
                arg.classType.classFqName?.asString() ?: run {
                    context.messageCollector.report(
                        CompilerMessageSeverity.ERROR,
                        "The given class does not have a qualified name.",
                        arg.getCompilerMessageLocation(currentFile) ?: expression.getCompilerMessageLocation(currentFile)
                    )
                    return super.visitCall(expression)
                }
            }
            return IrConstImpl(
                expression.startOffset,
                expression.endOffset,
                context.irBuiltIns.stringType,
                IrConstKind.String,
                constValue
            )
        }
        return super.visitCall(expression)
    }
}