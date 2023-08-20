package ly.david.data.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.getDateFormatted(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.getTimeFormatted(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(this)
}
