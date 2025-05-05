package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.MutableOrEmptyList
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.declarations.isJavaOrEnhancement
import org.jetbrains.kotlin.fir.declarations.utils.isConst
import org.jetbrains.kotlin.fir.expressions.FirPropertyAccessExpression
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.types.ConstantValueKind

class FirKonstStatusTransformerExtension(session: FirSession) : FirStatusTransformerExtension(session) {
    companion object {
        val suppressClassId = ClassId(FqName("kotlin"), FqName("Suppress"), false)
    }

    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        if (declaration.isJavaOrEnhancement) return false
        return session.konstPredicateMatcher.isAnnotated(declaration)
    }

    override fun transformStatus(
        status: FirDeclarationStatus,
        property: FirProperty,
        containingClass: FirClassLikeSymbol<*>?,
        isLocal: Boolean
    ): FirDeclarationStatus {
        if (property.isConst) {
//            addSuppressAnno(property)
            property.transform<FirStatement, Nothing?>(object: FirTransformer<Nothing?>() {
                override fun <E : FirElement> transformElement(
                    element: E,
                    data: Nothing?
                ): E {
                    element.transformChildren(this, data)
                    return element
                }

                override fun transformPropertyAccessExpression(
                    propertyAccessExpression: FirPropertyAccessExpression,
                    data: Nothing?
                ): FirStatement {
                    return FirKonstExpression(
                        propertyAccessExpression.source,
                        null,
                        MutableOrEmptyList(mutableListOf()),
                        ConstantValueKind.String,
                        null,
                        null,
                        propertyAccessExpression
                    )
                }
            }, null)
        }
        return status
    }

}