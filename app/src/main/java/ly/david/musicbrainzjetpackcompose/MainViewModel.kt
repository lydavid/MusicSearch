package ly.david.musicbrainzjetpackcompose

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.musicbrainz.Artist
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService

internal class MainViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    val artists = mutableStateListOf<Artist>()

    fun queryArtists(queryString: String) {
        viewModelScope.launch {
            val foundArtists = musicBrainzApiService.queryArtists(queryString)
            Log.d("debug", "queryArtists: $foundArtists")
            artists.clear()
            artists.addAll(foundArtists.artists)
        }
    }
}
