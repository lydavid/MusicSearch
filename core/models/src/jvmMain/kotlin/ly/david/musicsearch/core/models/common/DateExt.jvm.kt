package ly.david.musicsearch.core.models.common

import kotlinx.datetime.Instant
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
