package ly.david.mbjc.ui.artist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.domain.ArtistUiModel
import ly.david.mbjc.data.domain.getLifeSpanForDisplay
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.toFlagEmoji
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun ArtistCard(
    artist: ArtistUiModel,
    onArtistClick: ArtistUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onArtistClick(artist) },
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val (name, countryCode, disambiguation, type, lifeSpan) = createRefs()

            Text(
                text = artist.name,
                style = TextStyles.getCardTitleTextStyle(),
                modifier = Modifier
                    .constrainAs(name) {
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(countryCode.start)
                    }
            )

            if (!artist.countryCode.isNullOrEmpty()) {
                Text(
                    text = "${artist.countryCode.toFlagEmoji()} ${artist.countryCode}",
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier
                        .constrainAs(countryCode) {
                            width = Dimension.wrapContent
                            top.linkTo(parent.top)
                            start.linkTo(name.end, margin = 4.dp)
                            end.linkTo(parent.end)
                        }
                )
            } else {
                Spacer(modifier = Modifier.constrainAs(countryCode) {
                    end.linkTo(parent.end)
                })
            }

            if (!artist.disambiguation.isNullOrEmpty()) {
                Text(
                    text = artist.disambiguation.transformThisIfNotNullOrEmpty { "($it)" },
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor(),
                    modifier = Modifier
                        .constrainAs(disambiguation) {
                            width = Dimension.matchParent
                            top.linkTo(name.bottom, margin = 4.dp)
                        }
                )
            } else {
                Spacer(modifier = Modifier.constrainAs(disambiguation) {
                    top.linkTo(name.bottom)
                })
            }

            if (!artist.type.isNullOrEmpty()) {
                Text(
                    text = artist.type,
                    style = TextStyles.getCardBodySubTextStyle(),
                    modifier = Modifier
                        .constrainAs(type) {
                            width = Dimension.matchParent
                            top.linkTo(disambiguation.bottom, margin = 4.dp)
                        }
                )
            } else {
                Spacer(modifier = Modifier.constrainAs(type) {
                    top.linkTo(disambiguation.bottom)
                })
            }

            val lifeSpanText = artist.getLifeSpanForDisplay()
            if (lifeSpanText.isNotEmpty()) {
                Text(
                    text = lifeSpanText,
                    style = TextStyles.getCardBodySubTextStyle(),
                    modifier = Modifier
                        .constrainAs(lifeSpan) {
                            width = Dimension.matchParent
                            top.linkTo(type.bottom, margin = 4.dp)
                        }
                )
            }
        }
    }
}

internal class ArtistPreviewParameterProvider : PreviewParameterProvider<ArtistUiModel> {
    override val values = sequenceOf(
        ArtistUiModel(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
            countryCode = "CA"
        ),
        ArtistUiModel(
            id = "2",
            type = "Group, but for some reason it is really long and wraps around the screen",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "sort name should not be seen",
            disambiguation = "blah, blah, blah, some really long text that forces wrapping",
            countryCode = "XW",
            lifeSpan = LifeSpan(
                begin = "2020-12-31",
                end = "2022-01-01"
            )
        ),
        ArtistUiModel(
            id = "3",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = ""
        )
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview(
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: ArtistUiModel
) {
    PreviewTheme {
        Surface {
            ArtistCard(artist)
        }
    }
}
