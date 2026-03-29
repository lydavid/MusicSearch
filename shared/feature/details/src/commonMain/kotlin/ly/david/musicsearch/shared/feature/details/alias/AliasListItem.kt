package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
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
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.primary
import org.jetbrains.compose.resources.stringResource

@Composable
fun AliasListItem(
    alias: AliasListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    val primaryLabel = stringResource(Res.string.primary)

    ListItem(
        headlineContent = {
            Column {
                HighlightableText(
                    text = alias.name,
                    highlightedText = filterText,
                )

                val displayString = alias.getFormattedTypeAndLifeSpan(primaryLabel)
                displayString.ifNotEmpty { displayString ->
                    HighlightableText(
                        text = buildAnnotatedString {
                            append(displayString)
                            if (alias.isPrimary && primaryLabel.isNotEmpty()) {
                                val primaryStart = displayString.lastIndexOf(primaryLabel)
                                if (primaryStart >= 0) {
                                    addStyle(
                                        SpanStyle(fontWeight = FontWeight.SemiBold),
                                        start = primaryStart,
                                        end = primaryStart + primaryLabel.length,
                                    )
                                }
                            }
                        },
                        highlightedText = filterText,
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
