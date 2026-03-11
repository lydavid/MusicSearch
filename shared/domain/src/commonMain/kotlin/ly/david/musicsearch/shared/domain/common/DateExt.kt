package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlin.time.Clock
import kotlin.time.Instant

internal const val FULL_DATE_FORMAT = "EEEE, MMMM d, yyyy"
internal const val SHORT_DATE_FORMAT = "yyyy-MM-dd"
internal const val TIME_FORMAT = "hh:mm a"
internal const val DATE_TIME_WITH_SECONDS_FORMAT = "yyyy-MM-dd HH:mm:ss"

// See https://github.com/Kotlin/kotlinx-datetime/issues/211 for implementation details for jvm and ios.

expect fun Instant.getFullDateFormatted(timeZone: TimeZone = TimeZone.currentSystemDefault()): String

expect fun Instant.getShortDateFormatted(timeZone: TimeZone = TimeZone.currentSystemDefault()): String

expect fun Instant.getTimeFormatted(timeZone: TimeZone = TimeZone.currentSystemDefault()): String

expect fun Instant.getDateTimeFormatted(timeZone: TimeZone = TimeZone.currentSystemDefault()): String

fun Instant.getDateTimePeriod(
    now: Instant = Clock.System.now(),
): DateTimePeriod {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTimePeriod: DateTimePeriod = this.periodUntil(now, timeZone)
    return dateTimePeriod
}
