package ly.david.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun LocalRemoteProgressBar(
    totalRemote: Int?,
    totalLocal: Int,
    cachedLocalOfRemote: (Int, Int) -> String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        if (totalRemote == null) {
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                // TODO: better copy
                text = "No stats available. Tap this resource's tab to begin browsing."
            )
        } else {
            // TODO: "cached" is misleading here
            //  since the moment they click a release, it will require downloading details
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = cachedLocalOfRemote(totalLocal, totalRemote)
            )

            if (totalRemote != 0) {
                Surface(
                    shape = RoundedCornerShape(8.dp)
                ) {
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
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Empty() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 0,
                cachedLocalOfRemote = { local, remote ->
                    "Cached $local of $remote releases"
                },
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun Half() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 2,
                totalLocal = 1,
                cachedLocalOfRemote = { local, remote ->
                    "Cached $local of $remote releases"
                },
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun Full() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 1,
                cachedLocalOfRemote = { local, remote ->
                    "Cached $local of $remote releases"
                },
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun Overflow() {
    PreviewTheme {
        Surface {
            LocalRemoteProgressBar(
                totalRemote = 1,
                totalLocal = 2,
                cachedLocalOfRemote = { local, remote ->
                    "Cached $local of $remote releases"
                },
            )
        }
    }
}
// endregion
