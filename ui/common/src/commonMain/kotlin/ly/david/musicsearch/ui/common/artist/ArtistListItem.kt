package ly.david.musicsearch.ui.common.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.release.ReleaseListItem
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

/**
 * Displays the artist's image if it exists.
 * We don't request for it if it's missing unlike [ReleaseListItem] because it's too expensive,
 * requiring potentially 3 API calls, 2 of which are to rate-limited services (MB, Spotify).
 */
@Composable
fun ArtistListItem(
    artist: ArtistListItemModel,
    modifier: Modifier = Modifier,
    onClick: ArtistListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = artist.getAnnotatedName(),
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = artist.fontWeight,
            )
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onClick(artist)
                },
                onLongClick = {
                    onSelect(artist.id)
                },
            ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                artist.type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = artist.fontWeight,
                    )
                }

                val countryAndLifeSpan = listOfNotNull(
                    artist.countryCode?.let { "${it.toFlagEmoji()} $it" },
                    artist.lifeSpan.getLifeSpanForDisplay().takeIf { it.isNotEmpty() },
                ).joinToString("・")
                if (countryAndLifeSpan.isNotEmpty()) {
                    Text(
                        text = countryAndLifeSpan,
                        style = TextStyles.getCardBodySubTextStyle(),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = artist.imageUrl.orEmpty(),
                imageId = artist.imageId,
                placeholderIcon = MusicBrainzEntityType.ARTIST.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(artist.id)
                    },
                clipCircle = true,
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = artist,
                onClick = onEditCollectionClick,
            )
        },
    )
}
