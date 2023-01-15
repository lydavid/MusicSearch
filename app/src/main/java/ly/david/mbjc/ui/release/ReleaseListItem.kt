package ly.david.mbjc.ui.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toFlagEmoji
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.api.coverart.buildReleaseCoverArtUrl
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.coverart.SmallCoverArt
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

// TODO: we probably need preferences to show/hide some of the content in this item
// TODO: rethink showing release country -> could be misleading, and expensive joins
//  with cover art loaded by default, we can prob hide the other info by default
// TODO: we'll likely run into 429 when loading many images at once, not much we can do about that right now
//  see: https://tickets.metabrainz.org/browse/LB-1139 and https://tickets.metabrainz.org/browse/CAA-141
@Composable
internal fun ReleaseListItem(
    release: ReleaseListItemModel,
    showMoreInfo: Boolean = true,
    requestForMissingCoverArtPath: suspend () -> Unit = {},
    onClick: ReleaseListItemModel.() -> Unit = {}
) {

    LaunchedEffect(key1 = release.id) {
        if (release.coverArtPath == null) {
            requestForMissingCoverArtPath()
        }
    }

    ClickableListItem(
        onClick = { onClick(release) },
    ) {
        // TODO: don't need constraint
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val (coverArt, content) = createRefs()

            SmallCoverArt(
                modifier = Modifier
                    .constrainAs(coverArt) {
                        width = Dimension.value(64.dp)
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(content.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                    },
                placeholderIcon = Icons.Default.Album,
                coverArtUrl = buildReleaseCoverArtUrl(release.id, release.coverArtPath.orEmpty())
            )

            Column(
                modifier = Modifier.constrainAs(content) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(coverArt.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Text(
                    text = release.name,
                    style = TextStyles.getCardBodyTextStyle()
                )

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
                            // Since we don't store release events when browsing releases, releaseEvents will be empty until
                            // after we've clicked into it
                            val additionalReleaseEvents = if (release.releaseCountries.size > 1) {
                                "+ ${release.releaseCountries.size - 1}"
                            } else {
                                ""
                            }
                            Text(
                                text = "${countryCode.toFlagEmoji()} $countryCode" + additionalReleaseEvents.transformThisIfNotNullOrEmpty { " $it" },
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
                }

                // TODO: catalog number
//            Text(
//                text = release.name,
//                style = TextStyles.getCardBodyTextStyle(),
//            )
            }
        }
    }
}

// region Previews
internal class ReleasePreviewParameterProvider : PreviewParameterProvider<ReleaseListItemModel> {
    override val values: Sequence<ReleaseListItemModel> = sequenceOf(
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US"
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA"
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            date = "2021-09-08",
            countryCode = "JP",
            formattedFormats = "2×CD + Blu-ray",
            formattedTracks = "15 + 8 + 24"
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            formattedArtistCredits = "Some artist feat. Other artist"
        ),
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
            countryCode = "DZ",
            releaseCountries = listOf(
                ReleaseCountry("1", countryId = "2"),
                ReleaseCountry("1", countryId = "3"),
                ReleaseCountry("1", countryId = "4"),
            )
        ),
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: ReleaseListItemModel
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
