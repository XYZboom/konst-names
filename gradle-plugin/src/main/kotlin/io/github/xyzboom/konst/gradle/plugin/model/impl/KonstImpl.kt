package io.github.xyzboom.konst.gradle.plugin.model.impl

import io.github.xyzboom.konst.gradle.plugin.model.Konst
import java.io.Serializable

data class KonstImpl(
    override val name: String,
    override val annotations: List<String>,
) : Konst, Serializable {

    override val modelVersion = serialVersionUID

    companion object {
        private const val serialVersionUID = 1L
    }
}