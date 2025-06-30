package ly.david.musicsearch.ui.common.icon

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.Bookmark
import ly.david.musicsearch.ui.common.icons.BookmarkFilled
import ly.david.musicsearch.ui.common.icons.CustomIcons

@Composable
fun AddToCollectionIcon(
    partOfACollection: Boolean,
    modifier: Modifier = Modifier,
    nameWithDisambiguation: String = "",
) {
    val imageVector = if (partOfACollection) CustomIcons.BookmarkFilled else CustomIcons.Bookmark
    val tint = if (partOfACollection) MaterialTheme.colorScheme.primary else LocalContentColor.current
    val contentDescription = if (partOfACollection) {
        "Add $nameWithDisambiguation to another collection"
    } else {
        "Add $nameWithDisambiguation to collection"
    }
    Icon(
        imageVector = imageVector,
        tint = tint,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
