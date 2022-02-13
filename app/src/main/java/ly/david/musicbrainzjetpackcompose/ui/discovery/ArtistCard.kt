package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ly.david.musicbrainzjetpackcompose.musicbrainz.Artist
import ly.david.musicbrainzjetpackcompose.musicbrainz.LifeSpan
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun ArtistCard(artist: Artist) {
    Card {
        Text(text = artist.name)
    }
}

private val testArtist = Artist(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    score = 77,
    name = "月詠み",
    sortName = "Tsukuyomi",
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
