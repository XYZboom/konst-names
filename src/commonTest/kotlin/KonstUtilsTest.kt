package io.github.xyzboom.konst

import kotlin.test.Test
import kotlin.test.assertEquals

class KonstUtilsTest {
    @Test
    fun konstUtilsShouldBeCorrect() {
        assertEquals("KonstUtilsTest", simpleName<KonstUtilsTest>())
        assertEquals("io.github.xyzboom.konst.ksp.KonstUtilsTest", qualifiedName<KonstUtilsTest>())
    }
}