package io.github.xyzboom.konst.ksp

import com.google.devtools.ksp.symbol.KSDeclaration

@JvmInline
internal value class PackageName private constructor(val value: String) {
    companion object {
        @JvmStatic
        fun of(decl: KSDeclaration): PackageName {
            return PackageName(decl.packageName.asString())
        }
    }
}