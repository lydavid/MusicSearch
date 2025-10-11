package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.alias.AliasType
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AliasListItem(
    alias: BasicAlias,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val strings = LocalStrings.current

    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = alias.name,
                    style = TextStyles.getCardBodyTextStyle(),
                )

                val locale = alias.locale
                val displayLanguage = locale.getDisplayLanguage(strings)
                    .transformThisIfNotNullOrEmpty { "$it ($locale)" }
                val typeAndLifeSpan = listOfNotNull(
                    alias.type?.getDisplayString(strings),
                    alias.getLifeSpanForDisplay().takeIf { it.isNotEmpty() },
                    displayLanguage.takeIf { it.isNotEmpty() },
                    strings.primary.takeIf { alias.isPrimary },
                ).joinToString("・")
                typeAndLifeSpan.ifNotEmpty { typeAndLifeSpan ->
                    Text(
                        text = buildAnnotatedString {
                            append(typeAndLifeSpan)
                            if (alias.isPrimary && strings.primary.isNotEmpty()) {
                                val primaryStart = typeAndLifeSpan.lastIndexOf(strings.primary)
                                if (primaryStart >= 0) {
                                    addStyle(
                                        SpanStyle(fontWeight = FontWeight.SemiBold),
                                        start = primaryStart,
                                        end = primaryStart + strings.primary.length,
                                    )
                                }
                            }
                        },
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier
            .clickable {
                coroutineScope.launch {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboard.setClipEntry(clipEntryWith(alias.name))
                }
            },
    )
}

@Preview
@Composable
internal fun PreviewAliasListItemPrimary() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = BasicAlias(
                    name = "The Apothecary Diaries Season 2 Volume 2 (Original Anime Soundtrack)",
                    locale = "en",
                    isPrimary = true,
                    type = AliasType.RELEASE_GROUP_NAME,
                    begin = "",
                    end = "",
                    ended = false,
                ),
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewAliasListItemEnded() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = BasicAlias(
                    name = "なみ",
                    locale = "ja",
                    isPrimary = false,
                    type = AliasType.ARTIST_NAME,
                    begin = "2010",
                    end = "2015-02",
                    ended = true,
                ),
            )
        }
    }
}
