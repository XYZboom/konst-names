import io.github.xyzboom.konst.ksp.Konst
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Konst
class KonstTestForDefaultPackage {
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
        assertEquals(KonstTestForDefaultPackage_qName, KonstTestForDefaultPackage::class.qualifiedName!!)
        assertEquals(KonstTestForDefaultPackage_sName, KonstTestForDefaultPackage::class.simpleName!!)
        assertEquals(KonstTestForDefaultPackage_test_qName, "$KonstTestForDefaultPackage_qName.${KonstTestForDefaultPackage::test.name}")
        assertEquals(KonstTestForDefaultPackage_test_sName, KonstTestForDefaultPackage::test.name)
        assertEquals(KonstTestForDefaultPackage_testKonstNamesShouldBeCorrect_qName, "$KonstTestForDefaultPackage_qName.$testKonstNamesShouldBeCorrect")
        assertEquals(KonstTestForDefaultPackage_testKonstNamesShouldBeCorrect_sName, testKonstNamesShouldBeCorrect)
    }
}