package ly.david.musicsearch.ui.common.artist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemSimple() {
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
                id = "1",
                name = "artist name",
                sortName = "sort name should not be seen",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemCountryCode() {
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
                id = "1",
                name = "artist name",
                sortName = "sort name should not be seen",
                countryCode = "CA",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemAllInfoUnvisited() {
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
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
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemAllInfoVisited() {
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
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
                visited = true,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemSelected() {
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
                id = "1",
                name = "artist name",
                sortName = "sort name should not be seen",
            ),
            isSelected = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistListItemWithCoverArt() {
    InitializeFakeImageLoader()
    PreviewWithSharedElementTransition {
        ArtistListItem(
            artist = ArtistListItemModel(
                id = "1",
                name = "Artist name",
                sortName = "",
                imageUrl = "www.example.com/image",
            ),
        )
    }
}
