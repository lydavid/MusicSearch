package ly.david.ui.common.artist

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

internal class ArtistPreviewParameterProvider : PreviewParameterProvider<ArtistListItemModel> {
    override val values = sequenceOf(
        ArtistListItemModel(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
            countryCode = "CA",
        ),
        ArtistListItemModel(
            id = "2",
            type = "Group, but for some reason it is really long and wraps around the screen",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "sort name should not be seen",
            disambiguation = "blah, blah, blah, some really long text that forces wrapping",
            countryCode = "XW",
            lifeSpan = LifeSpanUiModel(
                begin = "2020-12-31",
                end = "2022-01-01",
            ),
        ),
        ArtistListItemModel(
            id = "3",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: ArtistListItemModel,
) {
    PreviewTheme {
        Surface {
            ArtistListItem(artist)
        }
    }
}
