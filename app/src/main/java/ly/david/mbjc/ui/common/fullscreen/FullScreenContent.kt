package ly.david.mbjc.ui.common.fullscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/**
 * For content that should fill the whole screen (to be precise, fill max size given to it).
 */
@Composable
fun FullScreenContent(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
fun FullScreenLoadingIndicator() {
    FullScreenContent {
        CircularProgressIndicator()
    }
}

@Composable
fun FullScreenText(
    text: String,
) {
    FullScreenContent {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            text = text
        )
    }
}
