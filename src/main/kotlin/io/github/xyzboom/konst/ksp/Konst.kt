package io.github.xyzboom.konst.ksp

import kotlin.annotation.AnnotationTarget.*

@Target(CLASS, PROPERTY, FIELD, FUNCTION, CONSTRUCTOR, FILE)
annotation class Konst {
    companion object {
        const val FILE_NAME = "konstNames"
    }
}
