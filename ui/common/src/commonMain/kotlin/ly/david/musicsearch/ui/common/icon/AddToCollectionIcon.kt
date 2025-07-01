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
    collected: Boolean,
    modifier: Modifier = Modifier,
    nameWithDisambiguation: String = "",
) {
    val imageVector = if (collected) CustomIcons.BookmarkFilled else CustomIcons.Bookmark
    val tint = if (collected) MaterialTheme.colorScheme.primary else LocalContentColor.current
    val contentDescription = if (collected) {
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
