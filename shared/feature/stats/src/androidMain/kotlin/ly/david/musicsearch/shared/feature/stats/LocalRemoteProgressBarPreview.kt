package ly.david.musicsearch.shared.feature.stats

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.feature.stats.internal.LocalRemoteProgressBar
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarEmpty() {
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

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarHalf() {
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

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarFull() {
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

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarOverflow() {
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
