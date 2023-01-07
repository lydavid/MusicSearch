package ly.david.mbjc.ui.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun LocalRemoteProgressBar(
    totalRemote: Int,
    totalLocal: Int,
) {
    if (totalRemote != 0) {
        LinearProgressIndicator(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(),
            progress = totalLocal / totalRemote.toFloat(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    }
}

// region Previews
@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Empty() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 0
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Half() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 2,
                totalLocal = 1
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Full() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 1
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Overflow() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 2
            )
        }
    }
}
// endregion
