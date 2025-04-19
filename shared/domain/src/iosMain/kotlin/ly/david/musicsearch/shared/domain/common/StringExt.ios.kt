package ly.david.musicsearch.shared.domain.common

/**
 * Converts a country code to flag emoji.
 * Copied from: [https://stackoverflow.com/a/50963795].
 *
 * @receiver An [ISO 3166-1 alpha-2 two-letter country code](https://en.wikipedia.org/wiki/Regional_Indicator_Symbol),
 * or "XW" for global, or "XE" for EU.
 * @return Flag emoji of country code, or globe emoji for global.
 */
actual fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    if (!this[0].isLetter() || !this[1].isLetter()) {
        return this
    }

    return when (val uppercaseCountryCode = this.uppercase()) {
        "XW" -> {
            "\uD83C\uDF10"
        }

        "XE" -> {
            "\uD83C\uDDEA\uD83C\uDDFA"
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
