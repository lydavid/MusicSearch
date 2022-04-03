package ly.david.mbjc.ui.artist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.domain.UiArtist
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.toFlagEmoji
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
fun ArtistCard(
    artist: UiArtist,
    onArtistClick: (UiArtist) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onArtistClick(artist) },
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val (name, disambiguation, type, countryCode) = createRefs()

            Text(
                text = artist.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .constrainAs(name) {
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(countryCode.start)
                    }
            )

            if (artist.countryCode != null) {
                Text(
                    text = "${artist.countryCode.toFlagEmoji()} ${artist.countryCode}",
                    style = MaterialTheme.typography.body1,
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

            if (artist.disambiguation != null) {
                Text(
                    text = artist.disambiguation.transformThisIfNotNullOrEmpty { "($it)" },
                    style = MaterialTheme.typography.body1,
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

            if (artist.type != null) {
                Text(
                    text = artist.type,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .constrainAs(type) {
                            width = Dimension.matchParent
                            top.linkTo(disambiguation.bottom, margin = 4.dp)
                        }
                )
            }


        }
    }
}

class ArtistPreviewParameterProvider : PreviewParameterProvider<UiArtist> {
    override val values = sequenceOf(
        UiArtist(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
            countryCode = "CA"
        ),
        UiArtist(
            id = "2",
            type = "Group, but for some reason it is really long and wraps",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "sort name should not be seen",
            disambiguation = "blah, blah, blah, some really long text that forces wrapping",
            countryCode = "XW",
            lifeSpan = LifeSpan(
                begin = "2020-10-10"
            )
        ),
        UiArtist(
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
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: UiArtist
) {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ArtistCard(artist)
        }
    }
}
