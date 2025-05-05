package io.github.xyzboom.konst

import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.name.FqName

fun IrAnnotationContainer.hasKonstAnno(annoFqNames: List<String>): Boolean {
    return annotations.any {
        (it.type.classFqName ?: return@any false).asString() in annoFqNames
    }
}

@OptIn(UnsafeDuringIrConstructionAPI::class)
fun IrCall.isKClassSimpleNameCall(): Boolean {
    return symbol.owner.fqNameWhenAvailable == FqName("kotlin.reflect.KClass.<get-simpleName>")
}

@OptIn(UnsafeDuringIrConstructionAPI::class)
fun IrCall.isKClassQualifiedNameCall(): Boolean {
    return symbol.owner.fqNameWhenAvailable == FqName("kotlin.reflect.KClass.<get-qualifiedName>")
}