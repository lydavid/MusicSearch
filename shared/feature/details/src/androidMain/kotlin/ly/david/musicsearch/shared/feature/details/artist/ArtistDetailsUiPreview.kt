package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ArtistDetailsUi(
                artist = ArtistScaffoldModel(
                    id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                    name = "The Beatles",
                    type = "Group",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1960",
                        end = "1970-04-10",
                        ended = true,
                    ),
                    sortName = "Beatles, The",
                ),
                artistImageUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433",
            )
        }
    }
}
// endregion
