package io.github.xyzboom.konst

import io.github.xyzboom.konst.ksp.Konst
import kotlin.test.Test
import kotlin.test.assertEquals

@Konst
class KonstTest {
    @Konst
    val test: String = "my string"

    /**
     * A same name in the same container will only be generated once.
     */
    @Konst
    val testKonstNamesShouldBeCorrect = "testKonstNamesShouldBeCorrect"

    @Konst
    @Test
    fun testKonstNamesShouldBeCorrect() {
        assertEquals(KonstTest_qName, KonstTest::class.qualifiedName!!)
        assertEquals(KonstTest_sName, KonstTest::class.simpleName!!)
        assertEquals(KonstTest_test_qName, "$KonstTest_qName.${KonstTest::test.name}")
        assertEquals(KonstTest_test_sName, KonstTest::test.name)
        assertEquals(KonstTest_testKonstNamesShouldBeCorrect_qName, "$KonstTest_qName.$testKonstNamesShouldBeCorrect")
        assertEquals(KonstTest_testKonstNamesShouldBeCorrect_sName, testKonstNamesShouldBeCorrect)
    }
}