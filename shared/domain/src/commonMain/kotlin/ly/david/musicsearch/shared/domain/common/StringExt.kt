package ly.david.musicsearch.shared.domain.common

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
expect fun String.toFlagEmoji(): String

fun String.appendOptionalText(optionalText: String?): String =
    this + optionalText.transformThisIfNotNullOrEmpty { " ($it)" }
