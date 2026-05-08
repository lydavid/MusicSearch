package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant

private fun Instant.getJavaLocalDateTime(timeZone: TimeZone): LocalDateTime = this.toLocalDateTime(
    timeZone = timeZone,
).toJavaLocalDateTime()

actual fun Instant.getDateTimeFormatted(
    format: DateTimeFormat,
    timeZone: TimeZone
): String {
    val javaLocalDataTime = getJavaLocalDateTime(timeZone)
    val dateFormat: FormatStyle?
    val timeFormat: FormatStyle?
    when (format) {
        DateTimeFormat.FullDate -> {
            dateFormat = FormatStyle.FULL
            timeFormat = null
        }
        DateTimeFormat.MediumDate -> {
            dateFormat = FormatStyle.MEDIUM
            timeFormat = null
        }
        DateTimeFormat.Time -> {
            dateFormat = null
            timeFormat = FormatStyle.SHORT
        }
        DateTimeFormat.MediumDateTime -> {
            val date = this.getDateTimeFormatted(DateTimeFormat.MediumDate, timeZone)
            val time = this.getDateTimeFormatted(DateTimeFormat.Time, timeZone)
            return "$date$DOT_SEPARATOR$time"
        }
    }

    return DateTimeFormatterBuilder()
        .appendLocalized(dateFormat, timeFormat)
        .toFormatter(Locale.getDefault())
        .format(javaLocalDataTime)
}
