package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.musicbrainzjetpackcompose.musicbrainz.Artist
import ly.david.musicbrainzjetpackcompose.musicbrainz.LifeSpan
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ArtistCard(
    artist: Artist,
    onClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick(artist.id) },
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            Text(
                text = artist.name,
                modifier = Modifier.fillMaxWidth()
            )

            if (artist.disambiguation != null) {
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "(${artist.disambiguation})",
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private val testArtist = Artist(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    score = 77,
    name = "Paracoccidioidomicosisproctitissarcomucosis paracoccidioidomicosisproctitissarcomucosisevenlonger",
    sortName = "Tsukuyomi",
    disambiguation = "blah, some really long text that forces wrapping",
    country = "JP",
    lifeSpan = LifeSpan(
        begin = "2020-10-10"
    )
)

@Preview(showBackground = true)
@Composable
internal fun Preview() {
    MusicBrainzJetpackComposeTheme {
        ArtistCard(testArtist)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun DarkPreview() {
    MusicBrainzJetpackComposeTheme {
        ArtistCard(testArtist)
    }
}
