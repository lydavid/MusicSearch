package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.MusicVideo
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.track.TrackListItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.lastListenedXAgo
import musicsearch.ui.common.generated.resources.video
import musicsearch.ui.common.generated.resources.xListens
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

/**
 * Also see [TrackListItem].
 */
@Composable
fun RecordingListItem(
    recording: RecordingListItemModel,
    filterText: String,
    now: Instant,
    showLastListenedPeriod: Boolean,
    modifier: Modifier = Modifier,
    boldUnvisited: Boolean = true,
    onRecordingClick: RecordingListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            HighlightableText(
                text = recording.getAnnotatedName(boldUnvisited = boldUnvisited),
                highlightedText = filterText,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        modifier = modifier.combinedClickable(
            onClick = { onRecordingClick(recording) },
            onLongClick = { onSelect(recording.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // TODO: highlight date when filtering for recording list item
                    //  I guess I skipped it because I didn't want to give the wrong idea that we can filter
                    //  on the formatted length
                    val dateAndLength = listOfNotNull(
                        recording.firstReleaseDate.takeIf { it.isNotEmpty() },
                        recording.length.toDisplayTime(),
                    ).joinToString(DOT_SEPARATOR)
                    Text(
                        text = dateAndLength,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )

                    if (recording.video) {
                        Icon(
                            imageVector = CustomIcons.MusicVideo,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(TINY_ICON_SIZE.dp),
                            contentDescription = stringResource(Res.string.video),
                        )
                    }
                }

                recording.formattedArtistCredits.ifNotNullOrEmpty {
                    HighlightableText(
                        modifier = Modifier.padding(top = 2.dp),
                        text = it,
                        highlightedText = filterText,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                recording.isrcs.ifNotEmpty {
                    HighlightableText(
                        modifier = Modifier.padding(top = 2.dp),
                        text = it.joinToString(", "),
                        highlightedText = filterText,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                val listenCount = recording.listenCount?.toInt()
                val formattedLastListenedPeriod = recording.lastListenedAtMs?.let {
                    formatPeriod(
                        Instant.fromEpochMilliseconds(it).getDateTimePeriod(now = now),
                    )
                }.takeIf { showLastListenedPeriod }
                if (listenCount != null) {
                    TextWithIcon(
                        modifier = Modifier.padding(top = 4.dp),
                        imageVector = CustomIcons.Headphones,
                        text = buildString {
                            append(listenCount.toString())
                            formattedLastListenedPeriod?.let {
                                append(DOT_SEPARATOR)
                                append(it)
                            }
                        },
                        mergedContentDescription = buildString {
                            append(pluralStringResource(Res.plurals.xListens, listenCount, listenCount))
                            formattedLastListenedPeriod?.let {
                                append("\n")
                                append(stringResource(Res.string.lastListenedXAgo, it))
                            }
                        },
                        iconSize = TINY_ICON_SIZE,
                        textStyle = TextStyles.getCardBodySubTextStyle(),
                    )

                    // TODO: show relatively how often you've listened to a recording compared to other recordings
                    //  by a browse method
                    //  Each details entity that can show a list of recordings need to fetch their most listened recording
                    //  and pass it here.
//                    LinearProgressIndicator(
//                        progress = { listenCount / maxOf(track.mostListenedTrackCount, 1).toFloat() },
//                        modifier = Modifier
//                            .height(4.dp)
//                            .fillMaxWidth(),
//                        color = MaterialTheme.colorScheme.primary,
//                        trackColor = Color.Transparent,
//                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                imageMetadata = null,
                placeholderIcon = MusicBrainzEntityType.RECORDING.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(recording.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = recording,
                onClick = onEditCollectionClick,
            )
        },
    )
}
