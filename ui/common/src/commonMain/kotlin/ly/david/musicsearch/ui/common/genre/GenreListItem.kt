package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun GenreListItem(
    genre: GenreListItemModel,
    modifier: Modifier = Modifier,
    onGenreClick: GenreListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = genre.getNameWithDisambiguation(),
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        },
        modifier = modifier.combinedClickable(
            onClick = { onGenreClick(genre) },
            onLongClick = { onSelect(genre.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = {
            ThumbnailImage(
                url = "",
                imageId = null,
                placeholderIcon = MusicBrainzEntity.GENRE.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(genre.id)
                    },
                isSelected = isSelected,
            )
        },
    )
}
