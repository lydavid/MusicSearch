package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.ui.common.button.RetryButton

@Composable
fun FullScreenErrorWithRetry(
    modifier: Modifier = Modifier,
    throwable: Throwable? = null,
    onClick: () -> Unit = {},
) {
    val error = when (throwable) {
        is HandledException -> {
            throwable.userMessage
        }

        else -> {
            "Couldn't fetch data from MusicBrainz.\nCome back later or click below to try again."
        }
    }
    FullScreenContent(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = error,
        )
        when (throwable) {
            is HandledException -> {
                when (throwable.errorResolution) {
                    ErrorResolution.Retry -> {
                        RetryButton(onClick = onClick)
                    }

                    else -> {
                        // No-op.
                    }
                }
            }

            else -> {
                RetryButton(onClick = onClick)
            }
        }
    }
}
