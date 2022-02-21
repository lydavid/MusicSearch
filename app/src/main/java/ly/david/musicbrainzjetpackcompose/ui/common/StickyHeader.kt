package ly.david.musicbrainzjetpackcompose.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.musicbrainzjetpackcompose.ui.theme.getSubBackgroundColor

@Composable
fun StickyHeader(text: String) {
    Surface(color = getSubBackgroundColor()) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview() {
    MusicBrainzJetpackComposeTheme {
        StickyHeader("Album + Compilation")
    }
}
