package ly.david.musicsearch.ui.common.locale

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.withStyle
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.ui.common.theme.getSubTextColor

private const val SCRIPT_LENGTH = 4

internal fun NameWithDisambiguationAndAliases.getAliasForLocale(
    systemLocale: Locale,
): String? {
    val systemTag = systemLocale.toLanguageTag()
    val systemLanguage = systemLocale.language

    /*
     * See https://en.wikipedia.org/wiki/IETF_language_tag for IETF BCP 47 language tag specs.
     */
    val alias = aliases.firstOrNull {
        // 1. Try to match exact language tag: {language}-{script}-{region}
        it.locale == systemTag
    }
        ?: // 2. Try to match: {language}-{script}
        aliases.firstOrNull {
            val parts = it.locale.split("-")
            val systemParts = systemTag.split("-")

            matches(parts, systemParts) &&
                parts[1].length == SCRIPT_LENGTH && systemParts[1].length == SCRIPT_LENGTH
        }
        ?: // 3. Try to match: {language}-{region}
        aliases.firstOrNull {
            val parts = it.locale.split("-")
            val systemParts = systemTag.split("-")

            matches(parts, systemParts)
            // We don't need to check the second part's length because having failed step 2,
            //  we must have a length of 2 if it exists
        }
        ?: // 4. Try to match: {language} only
        aliases.firstOrNull {
            it.locale.split("-")[0] == systemLanguage
        }
        ?: // 5. Fallback to any English variant
        aliases.firstOrNull {
            it.locale.startsWith("en")
        }

    return alias?.name?.takeIf { name?.contains(it) != true }
}

private fun matches(
    parts: List<String>,
    systemParts: List<String>,
): Boolean = parts.size >= 2 && systemParts.size >= 2 &&
    parts[0] == systemParts[0] && parts[1] == systemParts[1]

@Composable
fun NameWithDisambiguationAndAliases?.getAnnotatedName(): AnnotatedString {
    if (this == null) return AnnotatedString("")
    return buildAnnotatedString {
        append(name)

        val alias: String? = withAliases(
            aliases = aliases.filter { alias -> alias.isPrimary },
        ).getAliasForLocale(
            // Need to exclude locale from android:configChanges to force activity recreation for this to update
            // https://issuetracker.google.com/issues/240191036
            systemLocale = Locale.current,
        )
        if (!alias.isNullOrEmpty() || !disambiguation.isNullOrEmpty()) {
            withStyle(SpanStyle(color = getSubTextColor())) {
                append(" (")

                alias.ifNotNullOrEmpty {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(it)
                    }
                }

                if (!alias.isNullOrEmpty() && !disambiguation.isNullOrEmpty()) {
                    append(", ")
                }

                disambiguation.ifNotNullOrEmpty {
                    append(it)
                }

                append(")")
            }
        }
    }
}
