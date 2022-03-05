package ly.david.mbjc.ui.artist

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.UiState

// TODO: screen with information similar to this, just without release groups: https://musicbrainz.org/artist/6825ace2-3563-4ac5-8d85-c7bf1334bd2c
@Composable
fun ArtistOverviewScreen(
    artistId: String,
    onTitleUpdate: (title: String) -> Unit = {},
    viewModel: ArtistOverviewViewModel = hiltViewModel()
) {

    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.lookupArtist(artistId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { artist ->
                onTitleUpdate(artist.getNameWithDisambiguation())
            }
        }
        uiState.isLoading -> {
            FullScreenLoadingIndicator()
        }
        else -> {
            Text(text = "error...")
        }
    }
}
