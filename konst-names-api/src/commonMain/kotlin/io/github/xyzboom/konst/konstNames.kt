@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "unused")

package io.github.xyzboom.konst

import kotlin.internal.IntrinsicConstEvaluation
import kotlin.jvm.JvmSynthetic

@IntrinsicConstEvaluation
@JvmSynthetic
fun <T> simpleName(): String = intrinsicFailure()

@IntrinsicConstEvaluation
@JvmSynthetic
fun <T> qualifiedName(): String = intrinsicFailure()

private fun intrinsicFailure(): Nothing {
    throw UnsupportedOperationException("Property reference was not replaced by compiler. Did you apply Konst plugin?")
}