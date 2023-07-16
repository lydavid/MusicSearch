package ly.david.data.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DateExtTest {

    @Test
    fun `format date`() {
        val date = "2023-07-15 11:42:20".toDate()
        assertEquals("Saturday, July 15", date.getDateFormatted())
    }

    @Test
    fun `format time`() {
        val date = "2023-07-15 23:42:20".toDate()
        assertEquals("11:42 PM", date.getTimeFormatted())
    }
}
