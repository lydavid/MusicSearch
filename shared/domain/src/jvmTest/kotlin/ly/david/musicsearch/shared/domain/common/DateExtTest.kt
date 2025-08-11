package ly.david.musicsearch.shared.domain.common

import kotlin.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class DateExtTest {

    @Test
    fun `format date US`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.US)
        assertEquals("Friday, April 5", instant.getDateFormatted())
    }

    @Test
    fun `format time US`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.US)
        assertEquals("05:42 PM", instant.getTimeFormatted())
    }

    @Test
    fun `format date Canada`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.CANADA)
        assertEquals("Friday, April 5", instant.getDateFormatted())
    }

    @Test
    fun `format time Canada`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.CANADA)
        assertEquals("05:42 p.m.", instant.getTimeFormatted())
    }
}
