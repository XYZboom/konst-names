@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package io.github.xyzboom.konst

@kotlin.internal.IntrinsicConstEvaluation
@kotlin.internal.InlineOnly
inline fun <reified T> simpleName(): String {
    return T::class.simpleName!!
}

@kotlin.internal.IntrinsicConstEvaluation
@kotlin.internal.InlineOnly
inline fun <reified T> qualifiedName(): String {
    return T::class.qualifiedName!!
}
