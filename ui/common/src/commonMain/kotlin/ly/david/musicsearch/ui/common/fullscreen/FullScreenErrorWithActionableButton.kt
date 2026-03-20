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
import ly.david.musicsearch.ui.common.button.ButtonWithIcon
import ly.david.musicsearch.ui.common.button.RetryButton
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Login
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.login
import org.jetbrains.compose.resources.stringResource

@Composable
fun FullScreenErrorWithActionableButton(
    modifier: Modifier = Modifier,
    handledException: HandledException? = null,
    onRefresh: () -> Unit = {},
    onLogin: () -> Unit = {},
) {
    val error = when {
        handledException != null -> {
            handledException.userMessage
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
        when {
            handledException != null -> {
                when (handledException.errorResolution) {
                    ErrorResolution.Retry -> {
                        RetryButton(onClick = onRefresh)
                    }

                    ErrorResolution.Login -> {
                        ButtonWithIcon(
                            imageVector = CustomIcons.Login,
                            text = stringResource(Res.string.login),
                            onClick = onLogin,
                        )
                    }

                    else -> {
                        // No-op.
                    }
                }
            }

            else -> {
                RetryButton(onClick = onRefresh)
            }
        }
    }
}
