package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSDate
import kotlinx.datetime.toNSTimeZone
import platform.Foundation.NSDateFormatter
import kotlin.time.Instant

private fun getDateFormatter(timeZone: TimeZone): NSDateFormatter {
    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = timeZone.toNSTimeZone()
    return dateFormatter
}

actual fun Instant.getFullDateFormatted(timeZone: TimeZone): String {
    val dateFormatter = getDateFormatter(timeZone)
    dateFormatter.dateFormat = FULL_DATE_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getShortDateFormatted(timeZone: TimeZone): String {
    val dateFormatter = getDateFormatter(timeZone)
    dateFormatter.dateFormat = SHORT_DATE_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getTimeFormatted(timeZone: TimeZone): String {
    val dateFormatter = getDateFormatter(timeZone)
    dateFormatter.dateFormat = TIME_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getDateTimeFormatted(timeZone: TimeZone): String {
    val dateFormatter = getDateFormatter(timeZone)
    dateFormatter.dateFormat = DATE_TIME_WITH_SECONDS_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}
