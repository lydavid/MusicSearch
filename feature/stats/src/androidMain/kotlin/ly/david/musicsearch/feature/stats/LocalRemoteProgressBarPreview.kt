package ly.david.musicsearch.feature.stats

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.feature.stats.internal.LocalRemoteProgressBar
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
