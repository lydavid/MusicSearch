package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.track.TrackListItem
import ly.david.musicsearch.ui.core.theme.TextStyles

/**
 * Also see [TrackListItem].
 */
@Composable
fun RecordingListItem(
    recording: RecordingListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: RecordingListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = recording.name,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = recording.fontWeight,
            )
        },
        modifier = modifier.clickable {
            onRecordingClick(recording)
        },
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = recording.disambiguation,
                    fontWeight = recording.fontWeight,
                )

                recording.formattedArtistCredits.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = recording.fontWeight,
                    )
                }
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                recording.firstReleaseDate.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = recording.fontWeight,
                    )
                }

                Text(
                    text = recording.length.toDisplayTime(),
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = recording.fontWeight,
                )
            }
        },
    )
}
