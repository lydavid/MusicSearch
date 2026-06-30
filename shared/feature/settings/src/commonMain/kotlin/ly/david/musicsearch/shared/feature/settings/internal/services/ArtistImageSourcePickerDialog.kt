package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.retained.rememberRetained
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.ui.common.dialog.BasicDialog
import ly.david.musicsearch.ui.common.text.SingleLineTextField
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.artistImageSource
import musicsearch.ui.common.generated.resources.clientID
import musicsearch.ui.common.generated.resources.clientSecret
import musicsearch.ui.common.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistImageSourcePickerDialog(
    initialSelectedArtistImageSource: ArtistImageSource,
    showDefaultSpotifyOption: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: (ArtistImageSource) -> Unit = {},
) {
    var selectedArtistImageSource by rememberRetained { mutableStateOf(initialSelectedArtistImageSource) }
    var customSpotifyClientId by rememberSaveable { mutableStateOf("") }
    var customSpotifyClientSecret by rememberSaveable { mutableStateOf("") }

    BasicDialog(onDismiss = onDismiss) {
        Column(modifier = modifier.padding(24.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(Res.string.artistImageSource),
                style = TextStyles.getHeaderTextStyle(),
            )

            Column(modifier = Modifier.selectableGroup()) {
                RadioRowWithText(
                    label = "Wikimedia",
                    isSelected = selectedArtistImageSource == ArtistImageSource.Wikimedia,
                    onClick = {
                        selectedArtistImageSource = ArtistImageSource.Wikimedia
                    },
                )
                if (showDefaultSpotifyOption) {
                    RadioRowWithText(
                        label = getDefaultSpotifyString(),
                        isSelected = selectedArtistImageSource == ArtistImageSource.Spotify.Default,
                        onClick = {
                            selectedArtistImageSource = ArtistImageSource.Spotify.Default
                        },
                    )
                }
                RadioRowWithText(
                    label = getCustomSpotifyString(showDefaultSpotifyOption = showDefaultSpotifyOption),
                    isSelected = selectedArtistImageSource is ArtistImageSource.Spotify.Custom,
                    onClick = {
                        selectedArtistImageSource = ArtistImageSource.Spotify.Custom()
                    },
                )
            }

            if (selectedArtistImageSource is ArtistImageSource.Spotify.Custom) {
                SingleLineTextField(
                    textLabel = stringResource(Res.string.clientID),
                    text = customSpotifyClientId,
                    textHint = "",
                    onTextChange = {
                        customSpotifyClientId = it
                        selectedArtistImageSource =
                            (selectedArtistImageSource as ArtistImageSource.Spotify.Custom).copy(
                                clientId = customSpotifyClientId,
                            )
                    },
                )
                SingleLineTextField(
                    textLabel = stringResource(Res.string.clientSecret),
                    text = customSpotifyClientSecret,
                    textHint = "",
                    onTextChange = {
                        customSpotifyClientSecret = it
                        selectedArtistImageSource =
                            (selectedArtistImageSource as ArtistImageSource.Spotify.Custom).copy(
                                clientSecret = customSpotifyClientSecret,
                            )
                    },
                )
            }

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onConfirm(selectedArtistImageSource)
                    onDismiss()
                },
            ) {
                Text(stringResource(Res.string.ok))
            }
        }
    }
}
