package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter

actual fun Instant.getDateFormatted(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = DATE_FORMAT
    return dateFormatter.stringFromDate(
        date = toNSDate(),
    )
}

actual fun Instant.getTimeFormatted(): String {
    val dateFormatter = NSDateFormatter()
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
