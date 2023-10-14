package ly.david.ui.common.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun RetryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(Icons.Default.Refresh, "")
        Text(
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = strings.retry,
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            RetryButton()
        }
    }
}
