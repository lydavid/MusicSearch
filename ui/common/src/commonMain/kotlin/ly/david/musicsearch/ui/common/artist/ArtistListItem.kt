package ly.david.musicsearch.ui.common.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.core.theme.TextStyles

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
