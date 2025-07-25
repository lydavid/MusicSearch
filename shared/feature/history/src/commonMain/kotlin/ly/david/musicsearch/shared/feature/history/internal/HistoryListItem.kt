package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.getName
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun HistoryListItem(
    lookupHistory: LookupHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteItem: (LookupHistoryListItemModel) -> Unit = {},
) {
    val strings = LocalStrings.current

    SwipeToDeleteListItem(
        content = {
            ListItem(
                headlineContent = {
                    Text(
                        text = lookupHistory.title,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                },
                modifier = modifier.clickable {
                    onItemClick(
                        lookupHistory.entity,
                        lookupHistory.id,
                        lookupHistory.title,
                    )
                },
                supportingContent = {
                    val resource = lookupHistory.entity.getName(strings)
                    Text(
                        text = resource,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                },
                leadingContent = {
                    ThumbnailImage(
                        url = lookupHistory.imageUrl.orEmpty(),
                        imageId = lookupHistory.imageId,
                        placeholderIcon = lookupHistory.entity.getIcon(),
                        clipCircle = lookupHistory.entity == MusicBrainzEntity.ARTIST,
                    )
                },
                trailingContent = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = lookupHistory.lastAccessed.getTimeFormatted(),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                        Text(
                            text = lookupHistory.numberOfVisits.toString(),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                },
            )
        },
        onDelete = {
            onDeleteItem(lookupHistory)
        },
    )
}
