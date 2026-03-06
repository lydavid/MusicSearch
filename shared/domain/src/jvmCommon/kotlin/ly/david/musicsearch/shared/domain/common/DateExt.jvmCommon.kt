package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Instant

private fun Instant.getJavaLocalDateTime(inUtc: Boolean): LocalDateTime = this.toLocalDateTime(
    timeZone = if (inUtc) TimeZone.UTC else TimeZone.currentSystemDefault(),
).toJavaLocalDateTime()

actual fun Instant.getFullDateFormatted(inUtc: Boolean): String {
    val javaLocalDataTime = getJavaLocalDateTime(inUtc)
    return DateTimeFormatter.ofPattern(FULL_DATE_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getShortDateFormatted(inUtc: Boolean): String {
    val javaLocalDataTime = getJavaLocalDateTime(inUtc)
    return DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getTimeFormatted(inUtc: Boolean): String {
    val javaLocalDataTime = getJavaLocalDateTime(inUtc)
    return DateTimeFormatter.ofPattern(TIME_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getDateTimeFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(javaLocalDataTime)
}
