package ly.david.ui.common.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toFlagEmoji
import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.ui.common.coverart.ThumbnailImage
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

// Don't worry about repeated calls to fetch artist images, cause coil handles caching for us.
// I think our problem wasn't so much with image calls, but finding the url for the images.
// Would this make unnecessary calls? As long as it's cached by okhttp, then it shouldn't be a big deal.
@Composable
fun ArtistListItem(
    artist: ArtistListItemModel,
    modifier: Modifier = Modifier,
    onArtistClick: ArtistListItemModel.() -> Unit = {}
) {
    ListItem(
        leadingContent = {
            ThumbnailImage(
                coverArtUrl = "https://i.scdn.co/image/ab6761610000f1786761852cd2852fceb64e8cd9",
                clipCircle = true
            )
        },
        headlineContent = {
            Text(
                text = artist.name,
                style = TextStyles.getCardTitleTextStyle()
            )
        },
        modifier = modifier.clickable {
            onArtistClick(artist)
        },
        supportingContent = {
            Column {
                DisambiguationText(disambiguation = artist.disambiguation)

                artist.type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle()
                    )
                }

                artist.lifeSpan.ifNotNull {
                    Text(
                        text = it.getLifeSpanForDisplay(),
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        trailingContent = {
            artist.countryCode?.ifNotNullOrEmpty { countryCode ->
                Text(
                    text = "${countryCode.toFlagEmoji()} $countryCode",
                    style = TextStyles.getCardTitleTextStyle()
                )
            }
        }
    )
}

internal class ArtistPreviewParameterProvider : PreviewParameterProvider<ArtistListItemModel> {
    override val values = sequenceOf(
        ArtistListItemModel(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
            countryCode = "CA"
        ),
        ArtistListItemModel(
            id = "2",
            type = "Group, but for some reason it is really long and wraps around the screen",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "sort name should not be seen",
            disambiguation = "blah, blah, blah, some really long text that forces wrapping",
            countryCode = "XW",
            lifeSpan = LifeSpan(
                begin = "2020-12-31",
                end = "2022-01-01"
            )
        ),
        ArtistListItemModel(
            id = "3",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = ""
        )
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: ArtistListItemModel
) {
    PreviewTheme {
        Surface {
            ArtistListItem(artist)
        }
    }
}
