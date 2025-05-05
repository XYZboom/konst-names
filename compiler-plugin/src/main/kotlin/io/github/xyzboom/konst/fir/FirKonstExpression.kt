package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.MutableOrEmptyList
import org.jetbrains.kotlin.fir.builder.toMutableOrEmpty
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.UnresolvedExpressionTypeAccess
import org.jetbrains.kotlin.fir.resolve.transformers.body.resolve.FirImplicitAwareBodyResolveTransformer
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.fir.visitors.FirVisitor
import org.jetbrains.kotlin.fir.visitors.transformInplace
import org.jetbrains.kotlin.types.ConstantValueKind

@OptIn(UnresolvedExpressionTypeAccess::class)
internal class FirKonstExpression(
    override val source: KtSourceElement?,
    @property:UnresolvedExpressionTypeAccess
    override var coneTypeOrNull: ConeKotlinType?,
    override var annotations: MutableOrEmptyList<FirAnnotation>,
    override var kind: ConstantValueKind,
    override val value: Any?,
    override val prefix: String?,
    /**
     * represents the origin `SomeClass::class.simpleName` expression.
     */
    var originExpr: FirStatement
) : FirLiteralExpression() {

    override fun <E : FirElement, D> transform(transformer: FirTransformer<D>, data: D): E {
        val origin = originExpr
        val new = originExpr.transform<FirStatement, D>(transformer, data)
        if (origin !== new) {
            originExpr = new
        }
        val me = if (transformer is FirImplicitAwareBodyResolveTransformer) {
            this.transform<FirLiteralExpression, Nothing?>(FirKonstExpressionTransformer, null)
        } else {
            this
        }
        @Suppress("UNCHECKED_CAST")
        return transformer.transformLiteralExpression(me, data) as E
    }

    override fun <R, D> acceptChildren(visitor: FirVisitor<R, D>, data: D) {
        annotations.forEach { it.accept(visitor, data) }
        originExpr.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: FirTransformer<D>, data: D): FirKonstExpression {
        transformAnnotations(transformer, data)
        val origin = originExpr
        val new = originExpr.transform<FirStatement, D>(transformer, data)
        if (origin !== new) {
            originExpr = new
        }
        return this
    }

    override fun <D> transformAnnotations(transformer: FirTransformer<D>, data: D): FirKonstExpression {
        annotations.transformInplace(transformer, data)
        return this
    }

    override fun replaceConeTypeOrNull(newConeTypeOrNull: ConeKotlinType?) {
        coneTypeOrNull = newConeTypeOrNull
    }

    override fun replaceAnnotations(newAnnotations: List<FirAnnotation>) {
        annotations = newAnnotations.toMutableOrEmpty()
    }

    override fun replaceKind(newKind: ConstantValueKind) {
        kind = newKind
    }
}
