package ly.david.musicsearch.data.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtTest {

    // region getYear
    @Test
    fun `get year success`() {
        assertEquals("2021", "2021-09-08".getYear())
    }

    @Test
    fun `get year empty string`() {
        assertEquals("", "".getYear())
    }

    @Test
    fun `get year too short`() {
        assertEquals("", "202".getYear())
    }
    // endregion

    // region toFlagEmoji
    @Test
    fun `valid country code`() {
        assertEquals("\uD83C\uDDE8\uD83C\uDDE6", "CA".toFlagEmoji())
    }

    @Test
    fun `global code`() {
        assertEquals("\uD83C\uDF10", "XW".toFlagEmoji())
    }

    @Test
    fun `European Union code`() {
        assertEquals("\uD83C\uDDEA\uD83C\uDDFA", "XE".toFlagEmoji())
    }

    @Test
    fun `empty string`() {
        assertEquals("", "".toFlagEmoji())
    }

    @Test
    fun `invalid country code, 3 letters`() {
        assertEquals("CAN", "CAN".toFlagEmoji())
    }

    @Test
    fun `invalid country code, 1 letter`() {
        assertEquals("C", "C".toFlagEmoji())
    }

    @Test
    fun `invalid country code, first character is not letter`() {
        assertEquals("#C", "#C".toFlagEmoji())
    }

    @Test
    fun `invalid country code, second character is not letter`() {
        assertEquals("C3", "C3".toFlagEmoji())
    }
    // endregion

    // region useHttps
    @Test
    fun `convert http to https`() {
        assertEquals(
            "https://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg",
            "http://coverartarchive.org/release/f81cbdf9-4390-4738-b6b2-124f5bceafe3/30440812185.jpg".useHttps()
        )
    }
    // endregion
}
