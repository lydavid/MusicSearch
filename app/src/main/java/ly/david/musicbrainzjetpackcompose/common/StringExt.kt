package ly.david.musicbrainzjetpackcompose.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val YEAR_FIRST_INDEX = 0
private const val YEAR_LAST_INDEX = 4

/**
 * Converts a date string with format "2022-02-15" to year only string "2022".
 * Or empty string if incompatible.
 */
fun String.getYear(): String =
    if (length < YEAR_LAST_INDEX) {
        ""
    } else {
        substring(YEAR_FIRST_INDEX, YEAR_LAST_INDEX)
    }

// TODO: good idea to use but it doesn't seem like this is the only issue with coil
//  maybe we need to edit user-agent?
/**
 * Ensure we use https because Coil doesn't accept it otherwise.
 * Cover Art Archive gives us urls with http.
 */
fun String.useHttps(): String = replace("http://", "https://")

private const val MUSIC_BRAINZ_DATE_FORMAT = "yyyy-MM-dd"
private const val MUSIC_BRAINZ_YEAR_ONLY_FORMAT = "yyyy"

/**
 * Turns a Music Brainz string date field to [Date] object.
 */
fun String.toDate(): Date? {
    return when {
        isEmpty() -> null
        else -> try {
            val dateFormat = SimpleDateFormat(MUSIC_BRAINZ_DATE_FORMAT, Locale.getDefault())
            dateFormat.parse(this)
        } catch (ex: ParseException) {
            val dateFormat = SimpleDateFormat(MUSIC_BRAINZ_YEAR_ONLY_FORMAT, Locale.getDefault())
            dateFormat.parse(this)
        }
    }
}

inline fun String?.ifNotNullOrEmpty(block: (String) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}

inline fun String?.transformThisIfNotNullOrEmpty(block: (String) -> String): String {
    return if (!this.isNullOrEmpty()) {
        block(this)
    } else {
        ""
    }
}
