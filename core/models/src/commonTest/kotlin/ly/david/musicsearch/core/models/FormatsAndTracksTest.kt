package ly.david.musicsearch.core.models

import kotlin.test.Test
import kotlin.test.assertEquals


class FormatsAndTracksTest {

    @Test
    fun `null`() {
        assertEquals("", null.getFormatsForDisplay())
        assertEquals("", null.getTracksForDisplay())
    }

    @Test
    fun empty() {
        assertEquals("", listOf<String?>().getFormatsForDisplay())
        assertEquals("", listOf<Int>().getTracksForDisplay())
    }

    @Test
    fun `unknown format`() {
        assertEquals("", listOf<String?>(null).getFormatsForDisplay())
        assertEquals("1", listOf(1).getTracksForDisplay())
    }

    @Test
    fun `single CD`() {
        assertEquals("CD", listOf("CD").getFormatsForDisplay())
        assertEquals("15", listOf(15).getTracksForDisplay())
    }

    @Test
    fun `unknown format + CD`() {
        assertEquals("CD", listOf(null, "CD").getFormatsForDisplay())
        assertEquals("1 + 2", listOf(1, 2).getTracksForDisplay())
    }

    @Test
    fun `2 CDs`() {
        assertEquals("2×CD", listOf("CD", "CD").getFormatsForDisplay())
        assertEquals("15 + 8", listOf(15, 8).getTracksForDisplay())
    }

    @Test
    fun `2 CDs and 1 Blu ray`() {
        assertEquals("2×CD + Blu-ray", listOf("CD", "Blu-ray", "CD").getFormatsForDisplay())
        assertEquals("15 + 8 + 24", listOf(15, 8, 24).getTracksForDisplay())
    }
}
