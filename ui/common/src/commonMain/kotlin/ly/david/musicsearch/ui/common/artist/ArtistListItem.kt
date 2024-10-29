package ly.david.musicsearch.ui.common.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.release.ReleaseListItem
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.image.ThumbnailImage

/**
 * Displays the artist's image if it exists.
 * We don't request for it if it's missing unlike [ReleaseListItem] because it's too expensive,
 * requiring potentially 3 API calls, 2 of which are to rate-limited services (MB, Spotify).
 */
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
                fontWeight = artist.fontWeight,
            )
        },
        modifier = modifier.clickable {
            onArtistClick(artist)
        },
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = artist.disambiguation,
                    fontWeight = artist.fontWeight,
                )

                artist.type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = artist.fontWeight,
                    )
                }

                artist.lifeSpan.ifNotNull {
                    Text(
                        text = it.getLifeSpanForDisplay(),
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = artist.fontWeight,
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
        leadingContent = {
            ThumbnailImage(
                url = artist.imageUrl.orEmpty(),
                mbid = artist.id,
                placeholderIcon = MusicBrainzEntity.ARTIST.getIcon(),
                modifier = Modifier.clip(CircleShape),
            )
        },
    )
}
