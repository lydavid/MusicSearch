package ly.david.musicsearch.core.models.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import java.util.Locale

class DateExtTest {

    @Test
    fun `format date US`() {
        val date: Date = "2023-07-15 11:42:20".toDate()
        Locale.setDefault(Locale.US)
        assertEquals("Saturday, July 15", date.getDateFormatted())
    }

    @Test
    fun `format time US`() {
        val date: Date = "2023-07-15 23:42:20".toDate()
        Locale.setDefault(Locale.US)
        assertEquals("11:42 PM", date.getTimeFormatted())
    }

    @Test
    fun `format date Canada`() {
        val date: Date = "2023-07-15 11:42:20".toDate()
        Locale.setDefault(Locale.CANADA)
        assertEquals("Saturday, July 15", date.getDateFormatted())
    }

    @Test
    fun `format time Canada`() {
        val date: Date = "2023-07-15 23:42:20".toDate()
        Locale.setDefault(Locale.CANADA)
        assertEquals("11:42 p.m.", date.getTimeFormatted())
    }
}
