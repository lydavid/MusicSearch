package ly.david.mbjc.ui.common

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
internal fun String.getYear(): String =
    if (length < YEAR_LAST_INDEX) {
        ""
    } else {
        substring(YEAR_FIRST_INDEX, YEAR_LAST_INDEX)
    }

/**
 * Ensure we use https because Coil doesn't accept it otherwise.
 * Cover Art Archive gives us urls with http.
 */
internal fun String.useHttps(): String = replace("http://", "https://")

private const val MUSIC_BRAINZ_DATE_FORMAT = "yyyy-MM-dd"
private const val MUSIC_BRAINZ_YEAR_MONTH_FORMAT = "yyyy-MM-dd"
private const val MUSIC_BRAINZ_YEAR_ONLY_FORMAT = "yyyy"

// TODO: unused
/**
 * Turns a MusicBrainz string date field to [Date] object.
 */
internal fun String.toDate(): Date? {
    return when {
        isEmpty() -> null
        else -> try {
            val dateFormat = SimpleDateFormat(MUSIC_BRAINZ_DATE_FORMAT, Locale.getDefault())
            dateFormat.parse(this)
        } catch (ex: ParseException) {
            val dateFormat = SimpleDateFormat(MUSIC_BRAINZ_YEAR_MONTH_FORMAT, Locale.getDefault())
            dateFormat.parse(this)
        } catch (ex: ParseException) {
            val dateFormat = SimpleDateFormat(MUSIC_BRAINZ_YEAR_ONLY_FORMAT, Locale.getDefault())
            dateFormat.parse(this)
        }
    }
}

internal inline fun String?.ifNotNullOrEmpty(block: (String) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}

internal inline fun String?.transformThisIfNotNullOrEmpty(block: (String) -> String): String {
    return if (!this.isNullOrEmpty()) {
        block(this)
    } else {
        ""
    }
}

/**
 * Converts a country code to flag emoji.
 * Copied from: [https://stackoverflow.com/a/50963795].
 *
 * @receiver An [ISO 3166-1 alpha-2 two-letter country code](https://en.wikipedia.org/wiki/Regional_Indicator_Symbol),
 * or "XW" for global, or "XE" for EU.
 * @return Flag emoji of country code, or globe emoji for global.
 */
internal fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    if (this == "XW") return "\uD83C\uDF10"
    else if (this == "XE") return "\uD83C\uDDEA\uD83C\uDDFA"

    val countryCodeCaps = this.uppercase() // upper case is important because we are calculating offset
    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    // TODO: not tested (according to test coverage report)
    // Check if both characters are alphabet
    if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
        return this
    }

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
