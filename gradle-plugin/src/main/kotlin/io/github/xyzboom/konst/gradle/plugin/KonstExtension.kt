package io.github.xyzboom.konst.gradle.plugin

open class KonstExtension {
    internal val myAnnotations = mutableListOf<String>()

    open fun annotation(fqName: String) {
        myAnnotations.add(fqName)
    }

    open fun annotations(fqNames: List<String>) {
        myAnnotations.addAll(fqNames)
    }

    open fun annotations(vararg fqNames: String) {
        myAnnotations.addAll(fqNames)
    }
}