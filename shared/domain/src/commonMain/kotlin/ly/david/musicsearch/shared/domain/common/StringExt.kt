package ly.david.musicsearch.shared.domain.common

import ly.david.musicsearch.shared.domain.area.NonCountryAreaWithCode
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
 */
fun String.prependHttpsIfMissing(): String {
    return if (startsWith("https://")) {
        this
    } else {
        "https://$this"
    }
}

fun String.trimProtocolAndExtension(): String = this
    .removePrefix("https://")
    .removePrefix("http://")
    .removeSuffix(".jpg")

/**
 * Percent-encodes this string so it is safe to use as a component (segment) of a URL, following
 * the rules of [RFC 3986 section 3.3](https://www.rfc-editor.org/rfc/rfc3986#section-3.3).
 *
 * Only the unreserved characters - letters, digits, hyphen, underscore, dot and tilde - are kept
 * literal. Every other byte of the [UTF-8](https://www.rfc-editor.org/rfc/rfc3629) encoding is
 * replaced with its `%`-escaped representation, so multibyte characters such as CJK text are
 * encoded correctly.
 */
fun String.encodeUrlComponent(): String {
    val encodedBytes = encodeToByteArray()
    val result = StringBuilder(encodedBytes.size)
    for (byte in encodedBytes) {
        if (byte.isUrlComponentUnreserved()) {
            result.append(byte.toInt().toChar())
        } else {
            val hexValue = (byte.toInt() and UNSIGNED_BYTE_MASK).toString(URL_RADIX)
            result.append(PERCENT)
            result.append(hexValue.uppercase().padStart(HEX_LENGTH, HEX_PAD))
        }
    }
    return result.toString()
}

private fun Byte.isUrlComponentUnreserved(): Boolean {
    val char = toInt().toChar()
    return char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9' || char in URL_COMPONENT_SYMBOLS
}

private const val PERCENT = '%'
private const val HEX_PAD = '0'
private const val HEX_LENGTH = 2
private const val URL_RADIX = 16
private const val UNSIGNED_BYTE_MASK = 0xFF
private const val URL_COMPONENT_SYMBOLS = "-_.~"

inline fun String.ifNotEmpty(block: (String) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
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
@Suppress("MagicNumber")
fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    if (!this[0].isLetter() || !this[1].isLetter()) {
        return this
    }

    return when (this.uppercase()) {
        NonCountryAreaWithCode.Europe.code -> {
            "\uD83C\uDDEA\uD83C\uDDFA"
        }

        NonCountryAreaWithCode.Worldwide.code -> {
            "\uD83C\uDF10"
        }

        else -> {
            val unicodeRegionalIndicatorBase = 0x1F1E6
            val firstRegionalIndicator = (this[0] - 'A') + unicodeRegionalIndicatorBase
            val secondRegionalIndicator = (this[1] - 'A') + unicodeRegionalIndicatorBase

            buildString {
                appendCodePoint(firstRegionalIndicator)
                appendCodePoint(secondRegionalIndicator)
            }
        }
    }
}

// Written by Claude
@Suppress("MagicNumber")
private fun StringBuilder.appendCodePoint(codePoint: Int): StringBuilder {
    val basicMultilingualPlaneLimit = 0xFFFF
    if (codePoint <= basicMultilingualPlaneLimit) {
        append(codePoint.toChar())
    } else {
        // Split into surrogate pair
        val surrogateOffset = 0x10000
        val highSurrogateBase = 0xD800
        val lowSurrogateBase = 0xDC00
        val highSurrogate = (highSurrogateBase + ((codePoint - surrogateOffset) shr 10)).toChar()
        val lowSurrogate = (lowSurrogateBase + ((codePoint - surrogateOffset) and 0x3FF)).toChar()
        append(highSurrogate)
        append(lowSurrogate)
    }
    return this
}

fun String.appendOptionalText(optionalText: String?): String =
    this + optionalText.transformThisIfNotNullOrEmpty { " ($it)" }

/**
 * Converts possibly messy user-inputted MusicBrainz ID or URL to a UUID.
 */
@OptIn(ExperimentalUuidApi::class)
fun String.toUUID(): Uuid {
    val uuidRegex = Regex(
        pattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}",
        option = RegexOption.IGNORE_CASE,
    )
    val matchResult = uuidRegex.find(this)
    val uuidString = matchResult?.value?.lowercase()

    // .orEmpty() would say the input string is "" which be misleading,
    // so let this Uuid library give us its error with the original string when we fail to match.
    return Uuid.parse(uuidString ?: this)
}

expect fun String.decodeUrl(): String
