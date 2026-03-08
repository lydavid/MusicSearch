package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Instant

private fun Instant.getJavaLocalDateTime(timeZone: TimeZone): LocalDateTime = this.toLocalDateTime(
    timeZone = timeZone,
).toJavaLocalDateTime()

actual fun Instant.getFullDateFormatted(timeZone: TimeZone): String {
    val javaLocalDataTime = getJavaLocalDateTime(timeZone)
    return DateTimeFormatter.ofPattern(FULL_DATE_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getShortDateFormatted(timeZone: TimeZone): String {
    val javaLocalDataTime = getJavaLocalDateTime(timeZone)
    return DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getTimeFormatted(timeZone: TimeZone): String {
    val javaLocalDataTime = getJavaLocalDateTime(timeZone)
    return DateTimeFormatter.ofPattern(TIME_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getDateTimeFormatted(timeZone: TimeZone): String {
    val javaLocalDataTime = getJavaLocalDateTime(timeZone)
    return DateTimeFormatter.ofPattern(DATE_TIME_WITH_SECONDS_FORMAT).format(javaLocalDataTime)
}
