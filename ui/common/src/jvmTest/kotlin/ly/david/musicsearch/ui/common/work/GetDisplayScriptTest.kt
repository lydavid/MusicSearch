package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.shared.strings.EnStrings
import org.junit.Assert.assertEquals
import kotlin.test.Test

class GetDisplayScriptTest {

    private val strings: AppStrings = EnStrings

    @Test
    fun testLatinScript() {
        assertEquals(
            "Latin",
            "Latn".getDisplayScript(strings),
        )
    }

    @Test
    fun testJapaneseScript() {
        assertEquals(
            "Japanese",
            "Jpan".getDisplayScript(strings),
        )
    }

    @Test
    fun testKoreanScript() {
        assertEquals(
            "Korean",
            "Kore".getDisplayScript(strings),
        )
    }

    @Test
    fun testMultipleScripts() {
        assertEquals(
            "Multiple scripts",
            "Qaaa".getDisplayScript(strings),
        )
    }

    @Test
    fun testUnknownScript() {
        assertEquals(
            "",
            "Xyz".getDisplayScript(strings),
        )
    }
}
