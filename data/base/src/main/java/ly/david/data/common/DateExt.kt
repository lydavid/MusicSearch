package ly.david.data.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toDisplayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(this)
}
