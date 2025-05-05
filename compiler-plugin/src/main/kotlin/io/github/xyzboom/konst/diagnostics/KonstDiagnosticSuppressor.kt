package io.github.xyzboom.konst.diagnostics

import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.resolve.diagnostics.DiagnosticSuppressor

class KonstDiagnosticSuppressor : DiagnosticSuppressor {
    override fun isSuppressed(diagnostic: Diagnostic): Boolean {
        println()
        return false
    }

}