package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirBasicExpressionChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class FirKonstExpressionCheckersExtension(session: FirSession) : FirAdditionalCheckersExtension(session) {
    companion object {
        val suppressClassId = ClassId(FqName("kotlin"), FqName("Suppress"), false)
    }

    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val basicExpressionCheckers: Set<FirBasicExpressionChecker> =
            setOf(FirKonstPropertyAccessExpressionChecker())
    }

    inner class FirKonstPropertyAccessExpressionChecker : FirBasicExpressionChecker(MppCheckerKind.Common) {
        override fun check(
            expression: FirStatement,
            context: CheckerContext,
            reporter: DiagnosticReporter
        ) {
            if (context.containingDeclarations.any { session.konstPredicateMatcher.isAnnotated(it) }) {
                if (expression.source == null) {
                    return
                }
                expression.transform<FirExpression, Nothing?>(FirKonstExpressionTransformer, null)
            }
        }
    }

}