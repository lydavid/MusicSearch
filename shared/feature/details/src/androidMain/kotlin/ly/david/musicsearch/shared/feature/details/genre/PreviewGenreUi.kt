package ly.david.musicsearch.shared.feature.details.genre

import androidx.compose.runtime.Composable
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewGenreUi() {
    PreviewTheme {
        GenreUi(
            title = "pop",
            entityId = "911c7bbb-172d-4df8-9478-dbff4296e791",
            genre = GenreMusicBrainzModel(
                id = "911c7bbb-172d-4df8-9478-dbff4296e791",
                name = "pop",
            ),
        )
    }
}
