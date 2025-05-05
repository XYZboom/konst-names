package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.expressions.FirGetClassCall
import org.jetbrains.kotlin.fir.expressions.FirPropertyAccessExpression
import org.jetbrains.kotlin.fir.expressions.FirResolvedQualifier
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.references.resolved
import org.jetbrains.kotlin.fir.references.symbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.types.ConstantValueKind

object FirKonstExpressionTransformer : FirTransformer<Nothing?>() {
    override fun <E : FirElement> transformElement(element: E, data: Nothing?): E {
        return element
    }

    override fun transformPropertyAccessExpression(
        propertyAccessExpression: FirPropertyAccessExpression,
        data: Nothing?
    ): FirStatement {
        val symbol = propertyAccessExpression.calleeReference.resolved?.symbol as? FirPropertySymbol
            ?: return super.transformPropertyAccessExpression(propertyAccessExpression, data)
        val name = symbol.callableId.asSingleFqName().asString()
        val isKClassSimpleName = name == "kotlin.reflect.KClass.simpleName"
        val isKClassQualifiedName = name == "kotlin.reflect.KClass.qualifiedName"
        if (isKClassSimpleName || isKClassQualifiedName) {
            val explicitReceiver = propertyAccessExpression.explicitReceiver as? FirGetClassCall
                ?: return super.transformPropertyAccessExpression(propertyAccessExpression, data)
            val arguments = explicitReceiver.argumentList.arguments
            if (arguments.size != 1) {
                return super.transformPropertyAccessExpression(propertyAccessExpression, data)
            }
            val classId = (arguments.single() as? FirResolvedQualifier)?.classId
                ?: return super.transformPropertyAccessExpression(propertyAccessExpression, data)
            val isLocal = classId.isLocal || classId.packageFqName.asString() == "<local>"
            val (kind, value) = if (isKClassSimpleName) {
                ConstantValueKind.String to classId.relativeClassName.asString().replace("/", ".")
            } else {
                if (isLocal) {
                    ConstantValueKind.Null to null
                } else {
                    ConstantValueKind.String to classId.asFqNameString().replace("/", ".")
                }
            }
            return buildLiteralExpression(
                propertyAccessExpression.source,
                kind,
                value,
                propertyAccessExpression.annotations.toMutableList(),
                true
            )
        }

        return super.transformPropertyAccessExpression(propertyAccessExpression, data)
    }

}