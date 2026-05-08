package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlin.time.Clock
import kotlin.time.Instant

// See https://github.com/Kotlin/kotlinx-datetime/issues/211 for implementation details for jvm and ios.

expect fun Instant.getDateTimeFormatted(
    format: DateTimeFormat,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String

fun Instant.getDateTimePeriod(
    now: Instant = Clock.System.now(),
): DateTimePeriod {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTimePeriod: DateTimePeriod = this.periodUntil(now, timeZone)
    return dateTimePeriod
}
