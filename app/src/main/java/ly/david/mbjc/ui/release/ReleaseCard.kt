package ly.david.mbjc.ui.release

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import ly.david.mbjc.data.domain.UiRelease
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.toFlagEmoji
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
fun ReleaseCard(
    uiRelease: UiRelease,
    onClick: (UiRelease) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(uiRelease) },
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
                    text = uiRelease.name,
                    style = MaterialTheme.typography.h6,
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
                    val uiDisambiguation = uiRelease.disambiguation
                    if (uiDisambiguation.isNotEmpty()) {
                        Text(
                            text = "($uiDisambiguation)",
                            color = getSubTextColor(),
                            style = MaterialTheme.typography.body1,
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
                    val uiDate = uiRelease.date
                    if (!uiDate.isNullOrEmpty()) {
                        Text(
                            text = uiDate,
                            style = MaterialTheme.typography.body1,
                        )
                    }

                    val uiCountry = uiRelease.countryCode
                    if (!uiCountry.isNullOrEmpty()) {
                        if (!uiDate.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                        Text(
                            text = "${uiCountry.toFlagEmoji()} $uiCountry",
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }

            Row {
                val uiFormats = uiRelease.formats
                if (!uiFormats.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        text = uiFormats,
                        style = MaterialTheme.typography.body2,
                    )
                }

                // TODO: rename so it isn't named like our data class
                val uiTracks = uiRelease.tracks
                if (!uiTracks.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        text = uiTracks,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.End
                    )
                }
            }

        }
    }
}

class ReleasePreviewParameterProvider : PreviewParameterProvider<UiRelease> {
    override val values: Sequence<UiRelease> = sequenceOf(
        UiRelease(
            id = "1",
            name = "Release title",
            disambiguation = ""
        ),
        UiRelease(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        UiRelease(
            id = "1",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US"
        ),
        UiRelease(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA"
        ),
        UiRelease(
            id = "1",
            name = "Release title that is long and wraps",
            disambiguation = "Disambiguation text that is also long",
            date = "2021-09-08",
            countryCode = "JP",
            formats = "2×CD + Blu-ray",
            tracks = "15 + 8 + 24"
        ),
        UiRelease(
            id = "1",
            name = "Release title",
            date = "2022-04-03",
            disambiguation = "",
            countryCode = "NL",
        ),
        UiRelease(
            id = "1",
            name = "Release title",
            disambiguation = "",
            countryCode = "NL",
        ),
        UiRelease(
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
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: UiRelease
) {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseCard(release)
        }
    }
}
