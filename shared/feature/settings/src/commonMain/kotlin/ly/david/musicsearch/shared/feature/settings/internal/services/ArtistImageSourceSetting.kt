package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.artistImageSource
import musicsearch.ui.common.generated.resources.custom
import musicsearch.ui.common.generated.resources.default
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArtistImageSourceSetting(
    initialSelectedArtistImageSource: ArtistImageSource,
    showDefaultSpotifyOption: Boolean,
    modifier: Modifier = Modifier,
    onConfirm: (ArtistImageSource) -> Unit = {},
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        ArtistImageSourcePickerDialog(
            initialSelectedArtistImageSource = initialSelectedArtistImageSource,
            showDefaultSpotifyOption = showDefaultSpotifyOption,
            onConfirm = onConfirm,
            onDismiss = { showDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.artistImageSource),
            style = TextStyles.getCardBodyTextStyle(),
        )

        Text(
            text = when (initialSelectedArtistImageSource) {
                ArtistImageSource.Wikimedia -> {
                    "Wikimedia"
                }

                ArtistImageSource.Spotify.Default -> {
                    getDefaultSpotifyString()
                }

                is ArtistImageSource.Spotify.Custom -> {
                    getCustomSpotifyString(showDefaultSpotifyOption = showDefaultSpotifyOption)
                }
            },
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}

@Composable
internal fun getDefaultSpotifyString() = buildString {
    append("Spotify")
    append(" (${stringResource(Res.string.default)})")
}

@Composable
internal fun getCustomSpotifyString(
    showDefaultSpotifyOption: Boolean,
) = buildString {
    append("Spotify")
    if (showDefaultSpotifyOption) {
        append(" (${stringResource(Res.string.custom)})")
    }
}
