package ly.david.musicsearch.ui.common.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Refresh
import ly.david.musicsearch.ui.common.theme.LocalStrings

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
        Icon(CustomIcons.Refresh, "")
        Text(
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = strings.retry,
        )
    }
}
