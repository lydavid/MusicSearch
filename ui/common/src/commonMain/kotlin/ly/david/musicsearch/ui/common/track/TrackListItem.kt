package ly.david.musicsearch.ui.common.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

/**
 * Also see [RecordingListItem].
 */
@Composable
fun TrackListItem(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: (id: String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = track.getAnnotatedName(),
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = track.fontWeight,
            )
        },
        modifier = modifier.clickable {
            onRecordingClick(
                track.recordingId,
            )
        },
        leadingContent = {
            Text(
                text = track.number,
                style = TextStyles.getCardBodySubTextStyle(),
                fontWeight = track.fontWeight,
            )
        },
        trailingContent = {
            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodySubTextStyle(),
                fontWeight = track.fontWeight,
            )
            // TODO: support edit collection on underlying recording
        },
        supportingContent = {
            Column {
                track.formattedArtistCredits.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = track.fontWeight,
                    )
                }

                val listenCount = track.listenCount
                if (listenCount != null) {
                    TextWithIcon(
                        modifier = Modifier.padding(top = 4.dp),
                        imageVector = CustomIcons.Headphones,
                        text = listenCount.toString(),
                        iconSize = TINY_ICON_SIZE,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                    LinearProgressIndicator(
                        progress = { listenCount / maxOf(track.mostListenedTrackCount, 1).toFloat() },
                        modifier = Modifier
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
