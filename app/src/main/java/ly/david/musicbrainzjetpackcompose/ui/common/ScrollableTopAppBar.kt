package ly.david.musicbrainzjetpackcompose.ui.common

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO: pass in drawerclick
@Composable
fun ScrollableTopAppBar(
    title: String,
    subtitle: String = ""
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                Log.d("Remove This", "ScrollableTopAppBar: clicked menu")
            }) {
                Icon(Icons.Default.Menu, contentDescription = "Click to open navigation drawer.")
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ScrollableTopAppBar("A title", "A subtitle")
    }
}
