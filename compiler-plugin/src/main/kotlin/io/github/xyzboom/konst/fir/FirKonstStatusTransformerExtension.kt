package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.MutableOrEmptyList
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.declarations.isJavaOrEnhancement
import org.jetbrains.kotlin.fir.declarations.utils.isConst
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.types.ConstantValueKind

class FirKonstStatusTransformerExtension(session: FirSession) : FirStatusTransformerExtension(session) {
    companion object {
        object ShouldBeReplace
    }

    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        if (declaration.isJavaOrEnhancement) return false
        return session.konstPredicateMatcher.isAnnotated(declaration)
    }

    override fun transformStatus(
        status: FirDeclarationStatus,
        declaration: FirDeclaration
    ): FirDeclarationStatus {
        declaration.transform<FirElement, Nothing?>(object: FirTransformer<Nothing?>() {
            override fun <E : FirElement> transformElement(
                element: E,
                data: Nothing?
            ): E {
                element.transformChildren(this, data)
                return element
            }

            override fun transformProperty(property: FirProperty, data: Nothing?): FirStatement {
                if (property.isConst) {
                    val initializer = property.initializer ?: return property
                    property.replaceInitializer(FirKonstExpression(
                        initializer.source,
                        null,
                        MutableOrEmptyList(mutableListOf()),
                        ConstantValueKind.String,
                        ShouldBeReplace,
                        null,
                        initializer
                    ))
                }
                return property
            }
        }, null)
        return status
    }
}