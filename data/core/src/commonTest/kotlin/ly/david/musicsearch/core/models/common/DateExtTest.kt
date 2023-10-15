package ly.david.musicsearch.core.models.common

import ly.david.musicsearch.core.models.common.getDateFormatted
import ly.david.musicsearch.core.models.common.getTimeFormatted
import ly.david.musicsearch.core.models.common.toDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class DateExtTest {

    @Test
    fun `format date`() {
        val date: Date = "2023-07-15 11:42:20".toDate()
        assertEquals("Saturday, July 15", date.getDateFormatted())
    }

    @Test
    fun `format time`() {
        val date: Date = "2023-07-15 23:42:20".toDate()
        assertEquals("11:42 PM", date.getTimeFormatted())
    }
}
