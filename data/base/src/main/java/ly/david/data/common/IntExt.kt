package ly.david.data.common

private const val MS = 1000
private const val SECONDS_IN_MINUTE = 60
private const val MINUTES_IN_HOUR = 60

const val UNKNOWN_TIME = "?:??"

fun Int?.toDisplayTime(): String {
    if (this == null || this < 0) return UNKNOWN_TIME

    val timeWithoutMs = this / MS
    var minutes = timeWithoutMs / SECONDS_IN_MINUTE

    var hours = 0
    if (minutes >= MINUTES_IN_HOUR) {
        hours = minutes / MINUTES_IN_HOUR
        minutes %= hours
    }

    val seconds = timeWithoutMs % SECONDS_IN_MINUTE

    val formattedSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
    val formattedHours = if (hours > 0) "$hours:" else ""
    val minutesPadding = if (formattedHours.isNotEmpty() && minutes < 10) "0" else ""

    return "$formattedHours$minutesPadding$minutes:$formattedSeconds"
}
