package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.track.TrackListItem

/**
 * Also see [TrackListItem].
 */
@Composable
fun RecordingListItem(
    recording: RecordingListItemModel,
    modifier: Modifier = Modifier,
    onRecordingClick: RecordingListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = recording.getAnnotatedName(),
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = recording.fontWeight,
            )
        },
        modifier = modifier.combinedClickable(
            onClick = { onRecordingClick(recording) },
            onLongClick = { onSelect(recording.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                val dateAndLength = listOfNotNull(
                    recording.firstReleaseDate?.takeIf { it.isNotEmpty() },
                    recording.length.toDisplayTime(),
                ).joinToString("・")
                Text(
                    text = dateAndLength,
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = recording.fontWeight,
                )

                recording.formattedArtistCredits.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = recording.fontWeight,
                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = "",
                imageId = null,
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
