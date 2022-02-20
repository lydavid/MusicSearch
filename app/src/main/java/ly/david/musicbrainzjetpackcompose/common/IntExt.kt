package ly.david.musicbrainzjetpackcompose.common

private const val MS = 1000
private const val SECONDS_IN_MINUTE = 60
private const val MINUTES_IN_HOUR = 60

// todo: valid formats: 0:12, 59:59, 1:23:15, ?:??, 0:01
// TODO: test
fun Int?.toDisplayTime(): String {

    if (this == null) return "?:??"

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
