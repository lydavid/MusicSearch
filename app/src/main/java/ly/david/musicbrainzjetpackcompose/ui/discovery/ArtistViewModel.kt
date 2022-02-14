package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroup

class ArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    val totalFoundResults = mutableStateOf(0)

    val releaseGroups = mutableStateListOf<ReleaseGroup>()

    fun getReleaseGroupsByArtist(artistId: String) {
        viewModelScope.launch {
            val result = musicBrainzApiService.getReleaseGroupsByArtist(artistId)

            Log.d("Remove This", "getReleaseGroupsByArtist: ${result.releaseGroups}")
            releaseGroups.clear()
            releaseGroups.addAll(result.releaseGroups)
        }
    }
}
