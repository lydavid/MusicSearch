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
            val firstRegionalIndicator: Int = Character.codePointAt(uppercaseCountryCode, 0) - 0x41 + 0x1F1E6
            val secondRegionalIndicator: Int = Character.codePointAt(uppercaseCountryCode, 1) - 0x41 + 0x1F1E6

            String(Character.toChars(firstRegionalIndicator)) + String(Character.toChars(secondRegionalIndicator))
        }
    }
}
