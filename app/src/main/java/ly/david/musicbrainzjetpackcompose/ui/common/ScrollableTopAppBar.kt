package ly.david.musicbrainzjetpackcompose.ui.common

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScrollableTopAppBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        }
    )
}
