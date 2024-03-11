package ly.david.ui.common.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toDisplayTime
import ly.david.musicsearch.core.models.listitem.TrackListItemModel
import ly.david.ui.core.theme.TextStyles

/**
 * Also see [RecordingListItem].
 */
@Composable
fun TrackListItem(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {
    ListItem(
        headlineContent = {
            Text(
                text = track.title,
                style = TextStyles.getCardBodyTextStyle(),
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
            )
        },
        trailingContent = {
            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodySubTextStyle(),
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
                )
            }
        },
    )
}
