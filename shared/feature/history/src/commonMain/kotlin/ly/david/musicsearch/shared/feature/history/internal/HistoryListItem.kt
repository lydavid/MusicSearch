package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ly.david.musicsearch.shared.domain.common.getTimeFormatted
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.image.ThumbnailImage
import ly.david.musicsearch.ui.common.getDisplayText
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.image.getPlaceholderKey

@Composable
internal fun HistoryListItem(
    lookupHistory: LookupHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
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
                    val resource = lookupHistory.entity.getDisplayText(strings)
                    Text(
                        text = resource,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                },
                leadingContent = {
                    val clipModifier = if (lookupHistory.entity == MusicBrainzEntity.ARTIST) {
                        Modifier.clip(CircleShape)
                    } else {
                        Modifier
                    }
                    ThumbnailImage(
                        url = lookupHistory.imageUrl.orEmpty(),
                        placeholderKey = getPlaceholderKey(lookupHistory.id),
                        placeholderIcon = lookupHistory.entity.getIcon(),
                        modifier = clipModifier,
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
