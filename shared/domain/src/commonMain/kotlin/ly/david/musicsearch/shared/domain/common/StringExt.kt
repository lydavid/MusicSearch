package ly.david.musicsearch.shared.domain.common

import ly.david.musicsearch.shared.domain.area.NonCountryAreaWithCode

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
 * @receiver An [ISO 3166-1 alpha-2 two-letter country code](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2),
 * including the MusicBrainz special codes XE and XW from this [list](https://github.com/metabrainz/musicbrainz-server/tree/efb4f404d3905347c076f99c3c9904e75152b9cc/root/static/images/flags).
 * @return The [regional indicator symbol](https://en.wikipedia.org/wiki/Regional_Indicator_Symbol) flag emoji,
 * or globe emoji for global.
 */
fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    if (!this[0].isLetter() || !this[1].isLetter()) {
        return this
    }

    return when (val uppercaseCountryCode = this.uppercase()) {
        NonCountryAreaWithCode.Europe.code -> {
            "\uD83C\uDDEA\uD83C\uDDFA"
        }

        NonCountryAreaWithCode.Worldwide.code -> {
            "\uD83C\uDF10"
        }

        else -> {
            val firstRegionalIndicator = (this[0] - 'A') + 0x1F1E6
            val secondRegionalIndicator = (this[1] - 'A') + 0x1F1E6

            buildString {
                appendCodePoint(firstRegionalIndicator)
                appendCodePoint(secondRegionalIndicator)
            }
        }
    }
}

// Written by Claude
private fun StringBuilder.appendCodePoint(codePoint: Int): StringBuilder {
    if (codePoint <= 0xFFFF) {
        append(codePoint.toChar())
    } else {
        // Split into surrogate pair
        val highSurrogate = (0xD800 + ((codePoint - 0x10000) shr 10)).toChar()
        val lowSurrogate = (0xDC00 + ((codePoint - 0x10000) and 0x3FF)).toChar()
        append(highSurrogate)
        append(lowSurrogate)
    }
    return this
}

fun String.appendOptionalText(optionalText: String?): String =
    this + optionalText.transformThisIfNotNullOrEmpty { " ($it)" }
