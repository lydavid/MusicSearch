package ly.david.mbjc.ui.common

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
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getSubBackgroundColor

@Composable
fun ListSeparatorHeader(text: String) {
    Surface(color = getSubBackgroundColor()) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ListSeparatorHeaderPreview() {
    MusicBrainzJetpackComposeTheme {
        ListSeparatorHeader("Album + Compilation")
    }
}
