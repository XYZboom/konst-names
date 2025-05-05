package io.github.xyzboom.konst.fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.caches.FirCache
import org.jetbrains.kotlin.fir.caches.firCachesFactory
import org.jetbrains.kotlin.fir.caches.getValue
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.extensions.FirExtensionSessionComponent
import org.jetbrains.kotlin.fir.extensions.predicate.DeclarationPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.name.FqName

class FirKonstPredicateMatcher(
    session: FirSession,
    private val konstAnnotationFqNames: List<String>
) : FirExtensionSessionComponent(session) {
    companion object {
        fun getFactory(konstAnnotationFqNames: List<String>): Factory {
            return Factory { session -> FirKonstPredicateMatcher(session, konstAnnotationFqNames) }
        }
    }

    private val predicate = DeclarationPredicate.create {
        val annotationFqNames = konstAnnotationFqNames.map { FqName(it) }
        annotated(annotationFqNames) or metaAnnotated(annotationFqNames, includeItself = true)
    }

    private val declCache: FirCache<FirDeclaration, Boolean, Nothing?> =
        session.firCachesFactory.createCache { decl, _ ->
            session.predicateBasedProvider.matches(predicate, decl)
        }

    fun isAnnotated(decl: FirDeclaration): Boolean {
        return declCache.getValue(decl)
    }
}

val FirSession.konstPredicateMatcher: FirKonstPredicateMatcher by FirSession.sessionComponentAccessor()
