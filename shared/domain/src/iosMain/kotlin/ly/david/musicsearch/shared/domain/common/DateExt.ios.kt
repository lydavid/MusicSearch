package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSDate
import kotlinx.datetime.toNSTimeZone
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import kotlin.time.Instant

private fun getDateFormatter(timeZone: TimeZone): NSDateFormatter {
    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = timeZone.toNSTimeZone()
    return dateFormatter
}

actual fun Instant.getDateTimeFormatted(
    format: DateTimeFormat,
    timeZone: TimeZone,
): String {
    val dateFormatter = getDateFormatter(timeZone)
    when (format) {
        DateTimeFormat.FullDate -> {
            dateFormatter.dateStyle = NSDateFormatterFullStyle
            dateFormatter.timeStyle = NSDateFormatterNoStyle
        }
        DateTimeFormat.MediumDate -> {
            dateFormatter.dateStyle = NSDateFormatterMediumStyle
            dateFormatter.timeStyle = NSDateFormatterNoStyle
        }
        DateTimeFormat.Time -> {
            dateFormatter.dateStyle = NSDateFormatterNoStyle
            dateFormatter.timeStyle = NSDateFormatterShortStyle
        }
        DateTimeFormat.MediumDateTime -> {
            val date = this.getDateTimeFormatted(DateTimeFormat.MediumDate, timeZone)
            val time = this.getDateTimeFormatted(DateTimeFormat.Time, timeZone)
            return "$date$DOT_SEPARATOR$time"
        }
    }
    return dateFormatter.stringFromDate(toNSDate())
}
