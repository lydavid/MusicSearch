package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun ReleaseGroupListItem(
    releaseGroup: ReleaseGroupListItemModel,
    showType: Boolean,
    modifier: Modifier = Modifier,
    requestForMissingCoverArtUrl: suspend () -> Unit = {},
    onClick: ReleaseGroupListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    val latestRequestForMissingCoverArtUrl by rememberUpdatedState(requestForMissingCoverArtUrl)
    LaunchedEffect(key1 = releaseGroup.id) {
        if (releaseGroup.imageUrl == null) {
            latestRequestForMissingCoverArtUrl()
        }
    }

    ListItem(
        headlineContent = {
            Text(
                text = releaseGroup.name,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = releaseGroup.fontWeight,
            )
        },
        modifier = modifier.combinedClickable(
            onClick = { onClick(releaseGroup) },
            onLongClick = { onSelect(releaseGroup.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = releaseGroup.disambiguation,
                    fontWeight = releaseGroup.fontWeight,
                )

                if (showType) {
                    releaseGroup.getDisplayTypes().ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = releaseGroup.fontWeight,
                        )
                    }
                }

                releaseGroup.firstReleaseDate.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = releaseGroup.fontWeight,
                    )
                }

                releaseGroup.formattedArtistCredits.ifNotNull {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = releaseGroup.fontWeight,
                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = releaseGroup.imageUrl.orEmpty(),
                placeholderKey = releaseGroup.imageId,
                placeholderIcon = MusicBrainzEntity.RELEASE_GROUP.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(releaseGroup.id)
                    },
                isSelected = isSelected,
            )
        },
    )
}
