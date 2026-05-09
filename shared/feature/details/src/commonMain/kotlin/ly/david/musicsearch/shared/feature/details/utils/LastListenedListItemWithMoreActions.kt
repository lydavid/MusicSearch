package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.common.DateTimeFormat
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.ui.common.button.OverflowMenu
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.Mic
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.GoToScreenMenuItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.moreActionsFor
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastListenedListItemWithMoreActions(
    listenedMs: Long,
    recordingId: String,
    title: String,
    filterText: String,
    now: Instant,
    onClick: (recordingId: String) -> Unit,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
) {
    val instant = Instant.fromEpochMilliseconds(listenedMs)
    val formattedDateTimePeriod = formatPeriod(instant.getDateTimePeriod(now = now))
    val formattedDateTime = instant.getDateTimeFormatted(format = DateTimeFormat.MediumDateTime)
    val subtitle = "$formattedDateTimePeriod ($formattedDateTime)"
    val show = title.contains(filterText, ignoreCase = true) || subtitle.contains(filterText, ignoreCase = true)

    if (show) {
        ListItem(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        // If a listen shows up in the details screen, then it must have been linked to the entity
                        // through the recording, so this is valid.
                        onClick(recordingId)
                    },
                ),
            headlineContent = {
                HighlightableText(
                    text = title,
                    highlightedText = filterText,
                )
            },
            supportingContent = {
                HighlightableText(
                    text = subtitle,
                    highlightedText = filterText,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            },
            trailingContent = {
                OverflowMenu(
                    overflowDropdownMenuItems = {
                        GoToScreenMenuItem(
                            text = "Go to recording",
                            leadingIconImageVector = CustomIcons.Mic,
                            onClick = {
                                onClick(recordingId)
                            },
                        )
                        GoToScreenMenuItem(
                            text = "Go to listen",
                            leadingIconImageVector = CustomIcons.Headphones,
                            onClick = {
                                onGoToListenAtEpochSeconds(listenedMs / MS_IN_SECOND)
                            },
                        )
                    },
                    contentDescription = stringResource(Res.string.moreActionsFor, title),
                )
            },
        )
    }
}
