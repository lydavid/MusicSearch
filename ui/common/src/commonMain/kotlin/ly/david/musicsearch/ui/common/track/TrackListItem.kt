package ly.david.musicsearch.ui.common.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

/**
 * Also see [RecordingListItem].
 */
@Composable
fun TrackListItem(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: (id: String, title: String) -> Unit = { _, _ -> },
) {
    ListItem(
        headlineContent = {
            Text(
                text = track.title,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = track.fontWeight,
            )
        },
        modifier = modifier.clickable {
            onRecordingClick(
                track.recordingId,
                track.title,
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
        },
        supportingContent = {
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
        },
    )
}
