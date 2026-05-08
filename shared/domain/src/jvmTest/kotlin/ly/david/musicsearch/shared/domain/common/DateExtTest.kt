package ly.david.musicsearch.shared.domain.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale
import kotlin.time.Instant

class DateExtTest {

    @Test
    fun `format date time Canada`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.CANADA)
        assertEquals(
            "Friday, April 5, 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.FullDate),
        )
        assertEquals(
            "Apr 5, 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDate),
        )
        assertEquals(
            "5:42 p.m.",
            instant.getDateTimeFormatted(format = DateTimeFormat.Time),
        )
        assertEquals(
            "Apr 5, 2024・5:42 p.m.",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime),
        )
    }

    @Test
    fun `format date time US`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.US)
        assertEquals(
            "Friday, April 5, 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.FullDate),
        )
        assertEquals(
            "Apr 5, 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDate),
        )
        assertEquals(
            "5:42 PM",
            instant.getDateTimeFormatted(format = DateTimeFormat.Time),
        )
        assertEquals(
            "Apr 5, 2024・5:42 PM",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime),
        )
    }

    @Test
    fun `format date time France`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.FRANCE)
        assertEquals(
            "vendredi 5 avril 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.FullDate),
        )
        assertEquals(
            "5 avr. 2024",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDate),
        )
        assertEquals(
            "17:42",
            instant.getDateTimeFormatted(format = DateTimeFormat.Time),
        )
        assertEquals(
            "5 avr. 2024・17:42",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime),
        )
    }

    @Test
    fun `format date time Japan`() {
        val instant = Instant.parse("2024-04-05T21:42:20Z")
        Locale.setDefault(Locale.JAPAN)
        assertEquals(
            "2024年4月5日金曜日",
            instant.getDateTimeFormatted(format = DateTimeFormat.FullDate),
        )
        assertEquals(
            "2024/04/05",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDate),
        )
        assertEquals(
            "17:42",
            instant.getDateTimeFormatted(format = DateTimeFormat.Time),
        )
        assertEquals(
            "2024/04/05・17:42",
            instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime),
        )
    }
}
