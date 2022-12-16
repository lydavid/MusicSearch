package ly.david.mbjc.ui.artist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toFlagEmoji
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun ArtistListItem(
    artist: ArtistListItemModel,
    onArtistClick: ArtistListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onArtistClick(artist) },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val (mainSection, endSection, bottomSection) = createRefs()

            Text(
                text = artist.name,
                style = TextStyles.getCardTitleTextStyle(),
                modifier = Modifier
                    .constrainAs(mainSection) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(endSection.start)
                    }
            )

            artist.countryCode?.ifNotNullOrEmpty { countryCode ->
                Text(
                    text = "${countryCode.toFlagEmoji()} $countryCode",
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier
                        .constrainAs(endSection) {
                            width = Dimension.wrapContent
                            start.linkTo(mainSection.end, margin = 4.dp)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                )
            }

            Column(
                modifier = Modifier.constrainAs(bottomSection) {
                    width = Dimension.matchParent
                    top.linkTo(mainSection.bottom)
                }
            ) {
                DisambiguationText(disambiguation = artist.disambiguation)

                artist.type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle()
                    )
                }

                artist.lifeSpan.ifNotNull {
                    Text(
                        text = it.getLifeSpanForDisplay(),
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
internal class ArtistPreviewParameterProvider : PreviewParameterProvider<ArtistListItemModel> {
    override val values = sequenceOf(
        ArtistListItemModel(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
            countryCode = "CA"
        ),
        ArtistListItemModel(
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
        ArtistListItemModel(
            id = "3",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = ""
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview1(
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: ArtistListItemModel
) {
    PreviewTheme {
        Surface {
            ArtistListItem(artist)
        }
    }
}
