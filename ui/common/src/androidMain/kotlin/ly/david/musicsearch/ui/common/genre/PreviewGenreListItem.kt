package ly.david.musicsearch.ui.common.genre

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewGenreListItem() {
    PreviewTheme {
        Surface {
            GenreListItem(
                genre = GenreListItemModel(
                    id = "911c7bbb-172d-4df8-9478-dbff4296e791",
                    name = "pop",
                ),
            )
        }
    }
}
