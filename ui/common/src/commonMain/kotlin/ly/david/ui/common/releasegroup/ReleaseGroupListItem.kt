package ly.david.ui.common.releasegroup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.image.ThumbnailImage
import ly.david.ui.common.getIcon
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor

// TODO: have 2 modes: query and browse where some data is displayed differently
/**
 * A release group.
 *
 * Type is not displayed because it's displayed as a separator.
 */
@Composable
fun ReleaseGroupListItem(
    releaseGroup: ReleaseGroupListItemModel,
    modifier: Modifier = Modifier,
    requestForMissingCoverArtUrl: suspend () -> Unit = {},
    onClick: ReleaseGroupListItemModel.() -> Unit = {},
) {
    LaunchedEffect(key1 = releaseGroup.id) {
        if (releaseGroup.imageUrl == null) {
            requestForMissingCoverArtUrl()
        }
    }

    ListItem(
        headlineContent = {
            Text(
                text = releaseGroup.name,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        modifier = modifier.clickable { onClick(releaseGroup) },
        supportingContent = {
            Column {
                releaseGroup.disambiguation.ifNotNullOrEmpty {
                    Text(
                        text = "($it)",
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                releaseGroup.firstReleaseDate.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                releaseGroup.formattedArtistCredits.ifNotNull {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = releaseGroup.imageUrl.orEmpty(),
                mbid = releaseGroup.id,
                placeholderIcon = MusicBrainzEntity.RELEASE_GROUP.getIcon(),
            )
        },
    )
}
