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
import ly.david.data.common.toFlagEmoji
import ly.david.data.domain.ReleaseCardModel
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun ReleaseCard(
    release: ReleaseCardModel,
    onClick: ReleaseCardModel.() -> Unit = {}
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
                val (name, disambiguation, countryDate) = createRefs()

                Text(
                    text = release.name,
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier
                        .constrainAs(name) {
                            width = Dimension.fillToConstraints
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(countryDate.start)
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
                        .constrainAs(countryDate) {
                            width = Dimension.wrapContent
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                    horizontalAlignment = Alignment.End
                ) {
                    val uiDate = release.date
                    if (!uiDate.isNullOrEmpty()) {
                        Text(
                            text = uiDate,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }

                    val uiCountry = release.countryCode
                    if (!uiCountry.isNullOrEmpty()) {
                        if (!uiDate.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                        }

                        // Since we don't store release events when browsing releases, releaseEvents will be empty until
                        // after we've clicked into it
                        val additionalReleaseEvents = if (release.releaseEvents.size > 1) {
                            "+ ${release.releaseEvents.size - 1}"
                        } else {
                            ""
                        }
                        Text(
                            text = "${uiCountry.toFlagEmoji()} $uiCountry $additionalReleaseEvents",
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }
            }

            Row {
                val uiFormats = release.formats
                if (!uiFormats.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        text = uiFormats,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                val uiTracks = release.tracks
                if (!uiTracks.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        text = uiTracks,
                        style = TextStyles.getCardBodySubTextStyle(),
                        textAlign = TextAlign.End
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

internal class ReleasePreviewParameterProvider : PreviewParameterProvider<ReleaseCardModel> {
    override val values: Sequence<ReleaseCardModel> = sequenceOf(
        ReleaseCardModel(
            id = "1",
            name = "Release title",
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US"
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA"
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            date = "2021-09-08",
            countryCode = "JP",
            formats = "2×CD + Blu-ray",
            tracks = "15 + 8 + 24"
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            countryCode = "NL",
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            countryCode = "NL",
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
        ),
        ReleaseCardModel(
            id = "1",
            name = "Release title",
            countryCode = "DZ",
            releaseEvents = listOf(
                ReleaseCountry("1", countryId = "2"),
                ReleaseCountry("1", countryId = "3"),
                ReleaseCountry("1", countryId = "4"),
            )
        ),
    )
}

@DefaultPreviews
@Composable
private fun ReleaseCardPreview(
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: ReleaseCardModel
) {
    PreviewTheme {
        Surface {
            ReleaseCard(release)
        }
    }
}
