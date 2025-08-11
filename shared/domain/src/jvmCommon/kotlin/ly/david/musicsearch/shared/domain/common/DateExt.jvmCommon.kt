package ly.david.musicsearch.shared.domain.common

import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

actual fun Instant.getDateFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern(DATE_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getTimeFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern(TIME_FORMAT).format(javaLocalDataTime)
}

actual fun Instant.getDateTimeFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(javaLocalDataTime)
}
