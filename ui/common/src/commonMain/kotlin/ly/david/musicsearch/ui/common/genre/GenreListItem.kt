package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun GenreListItem(
    genre: GenreListItemModel,
    modifier: Modifier = Modifier,
    onGenreClick: GenreListItemModel.() -> Unit = {},
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
        modifier = modifier.clickable { onGenreClick(genre) },
    )
}
