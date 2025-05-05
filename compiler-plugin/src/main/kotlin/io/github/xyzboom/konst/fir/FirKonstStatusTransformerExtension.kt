package io.github.xyzboom.konst.fir

import io.github.xyzboom.konst.fir.FirKonstExpressionCheckersExtension.Companion.suppressClassId
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.declarations.isJavaOrEnhancement
import org.jetbrains.kotlin.fir.declarations.utils.isConst
import org.jetbrains.kotlin.fir.expressions.FirVarargArgumentsExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotationArgumentMapping
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotationCall
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildVarargArgumentsExpression
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.ConeTypeProjection
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneTypeOrNull
import org.jetbrains.kotlin.fir.types.constructClassType
import org.jetbrains.kotlin.fir.types.toLookupTag
import org.jetbrains.kotlin.fir.visitors.FirVisitorVoid
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.ConstantValueKind
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance
import kotlin.collections.set

class FirKonstStatusTransformerExtension(session: FirSession) : FirStatusTransformerExtension(session) {
    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        if (declaration.isJavaOrEnhancement) return false
        return session.konstPredicateMatcher.isAnnotated(declaration)
    }

    override fun transformStatus(status: FirDeclarationStatus, declaration: FirDeclaration): FirDeclarationStatus {
        addSuppressAnno(declaration)
        declaration.accept(object : FirVisitorVoid() {
            override fun visitElement(element: FirElement) {
                element.acceptChildren(this)
            }

            override fun visitProperty(property: FirProperty) {
                if (property.isConst) {
                    addSuppressAnno(declaration)

                }
                super.visitProperty(property)
            }
        })
        return status
    }

    fun addSuppressAnno(declaration: FirDeclaration) {
        val sourceElement = declaration.source
        val suppressAnno = declaration.annotations.firstOrNull { annotation ->
            val type = annotation.annotationTypeRef.coneTypeOrNull ?: return@firstOrNull false
            (type.classId ?: return@firstOrNull false).asFqNameString().replace("/", ".") == "kotlin.Suppress"
        }
        val suppressionList = listOf(
            "ERROR_SUPPRESSION",
            "CONST_VAL_WITH_NON_CONST_INITIALIZER",
            "ANNOTATION_ARGUMENT_MUST_BE_CONST"
        )
        val varargs = suppressionList.map {
            buildLiteralExpression(sourceElement, ConstantValueKind.String, it, setType = true)
        }
        if (suppressAnno != null) {
            val args = (suppressAnno.argumentMapping.mapping[Name.identifier("names")] as? FirVarargArgumentsExpression)
                ?.arguments ?: emptySet()
            suppressAnno.replaceArgumentMapping(
                buildAnnotationArgumentMapping {
                    mapping[Name.identifier("names")] = buildVarargArgumentsExpression {
                        arguments.addAll(args)
                        arguments.addAll(varargs)
                    }
                }
            )
            return
        }
        declaration.replaceAnnotations(
            listOf(
                *declaration.annotations.toTypedArray(),
                buildAnnotationCall {
                    containingDeclarationSymbol = declaration.symbol
                    annotationTypeRef = buildResolvedTypeRef {
                        coneType = suppressClassId.toLookupTag()
                            .constructClassType(
                                typeArguments = ConeTypeProjection.EMPTY_ARRAY,
                                isMarkedNullable = false
                            )
                    }
                    calleeReference = buildResolvedNamedReference {
                        source = sourceElement
                        name = Name.identifier("kotlin.Suppress")
                        val symbol =
                            session.symbolProvider.getClassLikeSymbolByClassId(suppressClassId) as FirRegularClassSymbol
                        resolvedSymbol = symbol.declarationSymbols.firstIsInstance<FirConstructorSymbol>()
                    }
                    argumentMapping = buildAnnotationArgumentMapping {
                        mapping[Name.identifier("names")] = buildVarargArgumentsExpression {
                            arguments.addAll(varargs)
                        }
                    }
                    argumentList = buildArgumentList {
                        arguments.addAll(varargs)
                    }
                }
            )
        )
    }
}