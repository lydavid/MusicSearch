package ly.david.musicsearch.ui.common.work

import org.junit.Assert.assertEquals
import kotlin.test.Test

class GetPlatformDisplayScriptTest {

    @Test
    fun testLatinScript() {
        assertEquals(
            "Latin",
            "Latn".getPlatformDisplayScript(),
        )
    }

    @Test
    fun testJapaneseScript() {
        assertEquals(
            "Japanese",
            "Jpan".getPlatformDisplayScript(),
        )
    }

    @Test
    fun testKoreanScript() {
        assertEquals(
            "Korean",
            "Kore".getPlatformDisplayScript(),
        )
    }

    @Test
    fun testUnknownScript() {
        assertEquals(
            "",
            "Xyz".getPlatformDisplayScript(),
        )
    }
}
