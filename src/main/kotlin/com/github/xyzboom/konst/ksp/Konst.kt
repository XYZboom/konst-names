package com.github.xyzboom.konst.ksp

import kotlin.annotation.AnnotationTarget.*

@Target(CLASS, PROPERTY, FIELD, FUNCTION, CONSTRUCTOR, FILE)
@Konst
annotation class Konst {
    companion object {
        const val FILE_NAME = "konstNames"
    }
}
