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
import ly.david.data.core.common.ifNotNull
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.common.toFlagEmoji
import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.data.core.getLifeSpanForDisplay
import ly.david.data.domain.LifeSpanUiModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun ArtistListItem(
    artist: ArtistListItemModel,
    modifier: Modifier = Modifier,
    onArtistClick: ArtistListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = artist.name,
                style = TextStyles.getCardBodyTextStyle()
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
                    style = TextStyles.getCardBodyTextStyle()
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
            lifeSpan = LifeSpanUiModel(
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
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: ArtistListItemModel,
) {
    PreviewTheme {
        Surface {
            ArtistListItem(artist)
        }
    }
}
