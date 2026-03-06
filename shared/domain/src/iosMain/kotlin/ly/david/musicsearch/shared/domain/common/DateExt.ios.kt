package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.timeZoneForSecondsFromGMT
import kotlin.time.Instant

private fun getDateFormatter(inUtc: Boolean): NSDateFormatter {
    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = if (inUtc) {
        NSTimeZone.timeZoneForSecondsFromGMT(0)
    } else {
        NSTimeZone.localTimeZone
    }
    return dateFormatter
}

actual fun Instant.getFullDateFormatted(inUtc: Boolean): String {
    val dateFormatter = getDateFormatter(inUtc)
    dateFormatter.dateFormat = FULL_DATE_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getShortDateFormatted(inUtc: Boolean): String {
    val dateFormatter = getDateFormatter(inUtc)
    dateFormatter.dateFormat = SHORT_DATE_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getTimeFormatted(inUtc: Boolean): String {
    val dateFormatter = getDateFormatter(inUtc)
    dateFormatter.dateFormat = TIME_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getDateTimeFormatted(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = DATE_TIME_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}
