package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewArtistImageSourcePickerDialogWikimedia() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ArtistImageSourcePickerDialog(
                initialSelectedArtistImageSource = ArtistImageSource.Wikimedia,
                showDefaultSpotifyOption = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistImageSourcePickerDialogDefaultSpotify() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ArtistImageSourcePickerDialog(
                initialSelectedArtistImageSource = ArtistImageSource.Spotify.Default,
                showDefaultSpotifyOption = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewArtistImageSourcePickerDialogCustomSpotify() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ArtistImageSourcePickerDialog(
                initialSelectedArtistImageSource = ArtistImageSource.Spotify.Custom(
                    clientId = "won't be",
                    clientSecret = "shown",
                ),
                showDefaultSpotifyOption = false,
            )
        }
    }
}
