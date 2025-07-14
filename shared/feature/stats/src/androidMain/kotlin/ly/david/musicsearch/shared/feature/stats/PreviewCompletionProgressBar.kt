package ly.david.musicsearch.shared.feature.stats

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarNoRemote() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = 0,
                currentCount = 0,
                formatProgressText = "Cached",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarEmpty() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = 1,
                currentCount = 0,
                formatProgressText = "Cached",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarHalf() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = 2,
                currentCount = 1,
                formatProgressText = "Cached",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarFull() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = 1,
                currentCount = 1,
                formatProgressText = "Cached",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarOverflow() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = 1,
                currentCount = 2,
                formatProgressText = "Cached",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLocalRemoteProgressBarUnknown() {
    PreviewTheme {
        Surface {
            CompletionProgressBar(
                totalCount = null,
                currentCount = 0,
                formatProgressText = "Cached",
            )
        }
    }
}
