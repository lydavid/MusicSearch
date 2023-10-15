package ly.david.musicsearch.core.models.common

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

/**
 * Ensure we use https because Coil doesn't accept it otherwise.
 * Cover Art Archive gives us urls with http.
 */
fun String.useHttps(): String = replace("http://", "https://")

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

fun String?.emptyToNull(): String? {
    return when {
        this == null -> null
        this.isEmpty() -> null
        else -> this
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
fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    if (!this[0].isLetter() || !this[1].isLetter()) {
        return this
    }
    val countryCodeCaps = this.uppercase() // upper case is important because we are calculating offset

    if (this == "XW") {
        return "\uD83C\uDF10"
    } else if (this == "XE") {
        return "\uD83C\uDDEA\uD83C\uDDFA"
    }

    // TODO: Support these flags
    //  https://github.com/metabrainz/musicbrainz-server/blob/f7d1d109e51b9c0313de3bdd64c1ce0f543e73f1/root/static/styles/flags.less

    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

fun String.toDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(this)
}
