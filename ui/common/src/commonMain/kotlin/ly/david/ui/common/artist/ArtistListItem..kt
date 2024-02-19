package ly.david.ui.common.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toFlagEmoji
import ly.david.musicsearch.core.models.getLifeSpanForDisplay
import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.ui.common.listitem.DisambiguationText
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
                style = TextStyles.getCardBodyTextStyle(),
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
                        style = TextStyles.getCardBodySubTextStyle(),
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
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        },
    )
}
