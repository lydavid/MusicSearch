package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.clickable
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
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.image.ThumbnailImage

// TODO: rethink showing release country -> could be misleading, and expensive joins
//  with cover art loaded by default, we can prob hide the other info by default
@Composable
fun ReleaseListItem(
    release: ReleaseListItemModel,
    modifier: Modifier = Modifier,
    showMoreInfo: Boolean = true,
    requestForMissingCoverArtUrl: suspend () -> Unit = {},
    onClick: ReleaseListItemModel.() -> Unit = {},
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
        modifier = modifier.clickable { onClick(release) },
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = release.disambiguation,
                    fontWeight = release.fontWeight,
                )

                if (showMoreInfo) {
                    Row {
                        release.date.ifNotNullOrEmpty {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = release.fontWeight,
                            )
                        }

                        release.countryCode.ifNotNullOrEmpty { countryCode ->
                            val count = release.releaseCountryCount
                            val additionalReleaseEvents = if (count > 1) {
                                "+ ${count - 1}"
                            } else {
                                ""
                            }
                            Text(
                                text = "${countryCode.toFlagEmoji()} $countryCode" +
                                    additionalReleaseEvents.transformThisIfNotNullOrEmpty { " $it" },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                style = TextStyles.getCardBodySubTextStyle(),
                                textAlign = TextAlign.End,
                                fontWeight = release.fontWeight,
                            )
                        }
                    }

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
                placeholderKey = release.imageId.toString(),
                placeholderIcon = MusicBrainzEntity.RELEASE.getIcon(),
            )
        },
    )
}
