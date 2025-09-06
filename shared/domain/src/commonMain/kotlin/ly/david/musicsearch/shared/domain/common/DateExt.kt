package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlin.time.Clock
import kotlin.time.Instant

internal const val DATE_FORMAT = "EEEE, MMMM d, YYYY"
internal const val TIME_FORMAT = "hh:mm a"
internal const val DATE_TIME_FORMAT = "YYYY-MM-dd HH:mm:ss"

// See https://github.com/Kotlin/kotlinx-datetime/issues/211 for implementation details for jvm and ios.

expect fun Instant.getDateFormatted(): String

expect fun Instant.getTimeFormatted(): String

expect fun Instant.getDateTimeFormatted(): String

fun Instant.getDateTimePeriod(
    now: Instant = Clock.System.now(),
): DateTimePeriod {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTimePeriod: DateTimePeriod = this.periodUntil(now, timeZone)
    return dateTimePeriod
}
