package ly.david.mbjc.ui.release

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toFlagEmoji
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun ReleaseListItem(
    release: ReleaseListItemModel,
    onClick: ReleaseListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(release) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (name, disambiguation, endSection) = createRefs()

                Text(
                    text = release.name,
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier
                        .constrainAs(name) {
                            width = Dimension.fillToConstraints
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(endSection.start)
                        }
                )

                Box(
                    modifier = Modifier
                        .constrainAs(disambiguation) {
                            width = Dimension.fillToConstraints
                            top.linkTo(name.bottom)
                            start.linkTo(name.start)
                            end.linkTo(name.end)
                        }
                ) {
                    val uiDisambiguation = release.disambiguation
                    if (uiDisambiguation.isNotEmpty()) {
                        Text(
                            text = "($uiDisambiguation)",
                            color = getSubTextColor(),
                            style = TextStyles.getCardBodyTextStyle(),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .constrainAs(endSection) {
                            width = Dimension.wrapContent
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                    horizontalAlignment = Alignment.End
                ) {
                    release.date.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }

                    release.countryCode.ifNotNullOrEmpty { countryCode ->
                        if (!release.date.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                        }

                        // Since we don't store release events when browsing releases, releaseEvents will be empty until
                        // after we've clicked into it
                        val additionalReleaseEvents = if (release.releaseCountries.size > 1) {
                            "+ ${release.releaseCountries.size - 1}"
                        } else {
                            ""
                        }
                        Text(
                            text = "${countryCode.toFlagEmoji()} $countryCode $additionalReleaseEvents",
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }
            }

            Row(modifier = Modifier.padding(top = 4.dp)) {
                release.formats.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                release.tracks.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.weight(1f),
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
                    style = TextStyles.getCardBodyTextStyle()
                )
            }

            // TODO: catalog number
//            Text(
//                text = release.name,
//                style = TextStyles.getCardBodyTextStyle(),
//            )
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
            formats = "2×CD + Blu-ray",
            tracks = "15 + 8 + 24"
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
            ReleaseListItem(release)
        }
    }
}
// endregion
