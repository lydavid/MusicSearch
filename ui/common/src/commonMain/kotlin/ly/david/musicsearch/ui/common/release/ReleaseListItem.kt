package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun ReleaseListItem(
    release: ReleaseListItemModel,
    modifier: Modifier = Modifier,
    showMoreInfo: Boolean = true,
    requestForMissingCoverArtUrl: suspend () -> Unit = {},
    onClick: ReleaseListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val latestRequestForMissingCoverArtUrl by rememberUpdatedState(requestForMissingCoverArtUrl)
    LaunchedEffect(key1 = release.id) {
        if (release.imageUrl == null) {
            latestRequestForMissingCoverArtUrl()
        }
    }

    ListItem(
        headlineContent = {
            Text(
                text = release.name,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = release.fontWeight,
            )
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onClick(release)
                },
                onLongClick = {
                    onSelect(release.id)
                },
            ),
        colors = listItemColors(isSelected = isSelected),
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = release.disambiguation,
                    fontWeight = release.fontWeight,
                )

                if (showMoreInfo) {
                    val countryAndDate = release.countryCode?.let { countryCode ->
                        buildString {
                            append("${countryCode.toFlagEmoji()} $countryCode")
                            if (release.releaseCountryCount > 1) append(" + ${release.releaseCountryCount - 1}")
                            if (release.date.isNotEmpty()) append("・${release.date}")
                        }
                    } ?: release.date
                    if (countryAndDate.isNotEmpty()) {
                        Text(
                            text = countryAndDate,
                            style = TextStyles.getCardBodySubTextStyle(),
                            textAlign = TextAlign.End,
                            fontWeight = release.fontWeight,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }

                    // TODO: formats/tracks count are not currently shown
                    //  consider showing at least in release group's releases to help disambiguate
                    Row {
                        release.formattedFormats.ifNotNullOrEmpty {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                text = it,
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = release.fontWeight,
                            )
                        }

                        release.formattedTracks.ifNotNullOrEmpty {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                text = it,
                                style = TextStyles.getCardBodySubTextStyle(),
                                textAlign = TextAlign.End,
                                fontWeight = release.fontWeight,
                            )
                        }
                    }

                    release.formattedArtistCredits.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = release.fontWeight,
                        )
                    }

                    release.catalogNumbers.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = release.fontWeight,
                        )
                    }
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = release.imageUrl.orEmpty(),
                imageId = release.imageId,
                placeholderIcon = MusicBrainzEntity.RELEASE.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(release.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = release,
                onClick = onEditCollectionClick,
            )
        },
    )
}
