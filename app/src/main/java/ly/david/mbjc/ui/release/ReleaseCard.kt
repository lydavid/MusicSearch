package ly.david.mbjc.ui.release

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.toFlagEmoji
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun ReleaseCard(
    releaseUiModel: ReleaseUiModel,
    onClick: ReleaseUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(releaseUiModel) },
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
                    text = releaseUiModel.name,
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
                    val uiDisambiguation = releaseUiModel.disambiguation
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
                    val uiDate = releaseUiModel.date
                    if (!uiDate.isNullOrEmpty()) {
                        Text(
                            text = uiDate,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }

                    val uiCountry = releaseUiModel.countryCode
                    if (!uiCountry.isNullOrEmpty()) {
                        if (!uiDate.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                        Text(
                            text = "${uiCountry.toFlagEmoji()} $uiCountry",
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }
            }

            Row {
                val uiFormats = releaseUiModel.formats
                if (!uiFormats.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        text = uiFormats,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                // TODO: rename so it isn't named like our data class
                val uiTracks = releaseUiModel.tracks
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

        }
    }
}

internal class ReleasePreviewParameterProvider : PreviewParameterProvider<ReleaseUiModel> {
    override val values: Sequence<ReleaseUiModel> = sequenceOf(
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            disambiguation = ""
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US"
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA"
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title that is long and wraps",
            disambiguation = "Disambiguation text that is also long",
            date = "2021-09-08",
            countryCode = "JP",
            formats = "2×CD + Blu-ray",
            tracks = "15 + 8 + 24"
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            disambiguation = "",
            countryCode = "NL",
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "NL",
        ),
        ReleaseUiModel(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            disambiguation = "",
        ),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview(
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: ReleaseUiModel
) {
    PreviewTheme {
        Surface {
            ReleaseCard(release)
        }
    }
}
