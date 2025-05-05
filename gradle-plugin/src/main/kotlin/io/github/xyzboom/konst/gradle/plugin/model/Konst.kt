package io.github.xyzboom.konst.gradle.plugin.model

interface Konst {

    val modelVersion: Long

    /**
     * Returns the module (Gradle project) name.
     *
     * @return the module name.
     */
    val name: String

    /**
     * Return the list of annotations.
     *
     * @return the list of annotations.
     */
    val annotations: List<String>
}