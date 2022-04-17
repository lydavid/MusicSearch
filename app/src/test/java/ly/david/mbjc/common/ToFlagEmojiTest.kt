package ly.david.mbjc.common

import ly.david.mbjc.ui.common.toFlagEmoji
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ToFlagEmojiTest {

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
    fun `invalid country code`() {
        assertEquals("CAN", "CAN".toFlagEmoji())
    }
}
