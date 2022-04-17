package ly.david.mbjc.common

import ly.david.mbjc.ui.common.toDisplayTime
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ToDisplayTimeTest {

    @Test
    fun `null`() {
        assertEquals("?:??", null.toDisplayTime())
    }

    @Test
    fun negative() {
        assertEquals("?:??", (-1 * 1000).toDisplayTime())
    }

    @Test
    fun zero() {
        assertEquals("0:00", 0.toDisplayTime())
    }

    @Test
    fun second() {
        assertEquals("0:01", (1 * 1000).toDisplayTime())
    }

    @Test
    fun `two digit seconds`() {
        assertEquals("0:10", (10 * 1000).toDisplayTime())
    }

    @Test
    fun minute() {
        assertEquals("1:00", (60 * 1000).toDisplayTime())
    }

    @Test
    fun `two digit minutes`() {
        assertEquals("10:00", (10 * 60 * 1000).toDisplayTime())
    }

    @Test
    fun hour() {
        assertEquals("1:00:00", (60 * 60 * 1000).toDisplayTime())
    }

    @Test
    fun `2 digit hours`() {
        assertEquals("10:00:00", (10 * 60 * 60 * 1000).toDisplayTime())
    }

    @Test
    fun `3 digit hours`() {
        assertEquals("100:00:00", (10 * 10 * 60 * 60 * 1000).toDisplayTime())
    }
}
