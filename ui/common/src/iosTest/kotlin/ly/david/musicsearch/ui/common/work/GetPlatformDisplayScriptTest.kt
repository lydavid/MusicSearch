package ly.david.musicsearch.ui.common.work

import kotlin.test.Test
import kotlin.test.assertEquals

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
            null,
            "Xyz".getPlatformDisplayScript(),
        )
    }
}
