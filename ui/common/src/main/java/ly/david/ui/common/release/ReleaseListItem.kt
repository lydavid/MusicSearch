package ly.david.ui.common.release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.common.toFlagEmoji
import ly.david.musicsearch.data.core.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.ReleaseListItemModel
import ly.david.ui.common.getIcon
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor
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
    LaunchedEffect(key1 = release.id) {
        if (release.imageUrl == null) {
            requestForMissingCoverArtUrl()
        }
    }

    ListItem(
        headlineContent = {
            Text(
                text = release.name,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        modifier = modifier.clickable { onClick(release) },
        supportingContent = {
            Column {
                release.disambiguation.ifNotNullOrEmpty {
                    Text(
                        text = "($it)",
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle()
                    )
                }

                if (showMoreInfo) {
                    Row {
                        release.date.ifNotNullOrEmpty {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                style = TextStyles.getCardBodySubTextStyle(),
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
                                textAlign = TextAlign.End
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
                            )
                        }

                        release.formattedTracks.ifNotNullOrEmpty {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .weight(1f),
                                text = it,
                                style = TextStyles.getCardBodySubTextStyle(),
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    release.formattedArtistCredits.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            style = TextStyles.getCardBodySubTextStyle()
                        )
                    }

                    // TODO: catalog number
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = release.imageUrl.orEmpty(),
                mbid = release.id,
                placeholderIcon = MusicBrainzEntity.RELEASE.getIcon(),
            )
        }
    )
}

// region Previews
internal class ReleasePreviewParameterProvider : PreviewParameterProvider<ReleaseListItemModel> {
    override val values: Sequence<ReleaseListItemModel> = sequenceOf(
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
        ),
        ReleaseListItemModel(
            id = "2",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        ReleaseListItemModel(
            id = "3",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US"
        ),
        ReleaseListItemModel(
            id = "4",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA"
        ),
        ReleaseListItemModel(
            id = "5",
            name = "Release title",
            date = "2021-09-08",
            countryCode = "JP",
            formattedFormats = "2×CD + Blu-ray",
            formattedTracks = "15 + 8 + 24"
        ),
        ReleaseListItemModel(
            id = "6",
            name = "Release title",
            date = "2022-04-03",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "7",
            name = "Release title",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "8",
            name = "Release title",
            date = "2022-04-03",
            formattedArtistCredits = "Some artist feat. Other artist"
        ),
        ReleaseListItemModel(
            id = "9",
            name = "Release title",
            countryCode = "DZ",
            releaseCountryCount = 3,
            formattedArtistCredits = "Some artist feat. another"
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: ReleaseListItemModel,
) {
    PreviewTheme {
        Surface {
            ReleaseListItem(
                release = release,
                showMoreInfo = true
            )
        }
    }
}
// endregion
