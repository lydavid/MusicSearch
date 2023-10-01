package ly.david.data.core.common

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime

fun Date.getDateFormatted(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.getTimeFormatted(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(this)
}

// https://github.com/Kotlin/kotlinx-datetime/issues/211
fun Instant.getDateFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern("EEEE, MMMM d").format(javaLocalDataTime)
}

fun Instant.getTimeFormatted(): String {
    val javaLocalDataTime = this.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
    return DateTimeFormatter.ofPattern("hh:mm a").format(javaLocalDataTime)
}
