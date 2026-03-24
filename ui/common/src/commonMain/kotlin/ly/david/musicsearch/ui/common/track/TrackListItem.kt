package ly.david.musicsearch.ui.common.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.icon.CollectionIcon
import ly.david.musicsearch.ui.common.icons.CheckCircle
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.SelectableId
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.moreActionsFor
import org.jetbrains.compose.resources.stringResource

/**
 * Also see [RecordingListItem].
 */
@Composable
fun TrackListItem(
    track: TrackListItemModel,
    mostListenedTrackCount: Long,
    modifier: Modifier = Modifier,
    onRecordingClick: (recordingId: String) -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (SelectableId) -> Unit = {},
    onClickMoreActions: TrackListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Row {
                TrackTitleWithLength(track = track)
            }
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onRecordingClick(track.recordingId)
                },
                onLongClick = {
                    onSelect(SelectableId(id = track.id, recordingId = track.recordingId))
                },
            ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = {
            TrackNumber(
                track = track,
                isSelected = isSelected,
                onClick = {
                    onSelect(SelectableId(id = track.id, recordingId = track.recordingId))
                },
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onClickMoreActions(track)
                },
            ) {
                Icon(
                    imageVector = CustomIcons.MoreVert,
                    contentDescription = stringResource(Res.string.moreActionsFor, track.name),
                )
            }
        },
        supportingContent = {
            Column {
                Text(
                    text = track.artists.getDisplayNames(),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = track.fontWeight,
                )

                val listenCount = track.listenCount
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    if (listenCount != null) {
                        TextWithIcon(
                            imageVector = CustomIcons.Headphones,
                            text = listenCount.toString(),
                            iconSize = TINY_ICON_SIZE,
                            textStyle = TextStyles.getCardBodySubTextStyle(),
                            modifier = Modifier
                                .padding(end = 4.dp),
                        )
                    }

                    CollectionIcon(
                        collected = track.collected,
                        modifier = Modifier
                            .size(TINY_ICON_SIZE.dp),
                    )
                }
                if (listenCount != null) {
                    LinearProgressIndicator(
                        progress = { listenCount / maxOf(mostListenedTrackCount, 1).toFloat() },
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .height(4.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.Transparent,
                    )
                }
            }
        },
    )
}

@Composable
fun TrackTitleWithLength(track: TrackListItemModel) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = TextStyles.getCardBodyTextStyle().fontSize)) {
                append(track.getAnnotatedName())
            }
            withStyle(style = SpanStyle(fontSize = TextStyles.getCardBodySubTextStyle().fontSize)) {
                append(" ${track.length.toDisplayTime()}")
            }
        },
        fontWeight = track.fontWeight,
    )
}

@Composable
fun TrackNumber(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(SMALL_IMAGE_SIZE.dp)
            .clip(CircleShape)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.size(SMALL_IMAGE_SIZE.dp),
                imageVector = CustomIcons.CheckCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "selected",
            )
        } else {
            Text(
                text = track.number,
                style = TextStyles.getHeaderTextStyle(),
                fontWeight = track.fontWeight,
                textAlign = TextAlign.Center,
            )
        }
    }
}
