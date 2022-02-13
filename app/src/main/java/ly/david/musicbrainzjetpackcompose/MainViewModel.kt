package ly.david.musicbrainzjetpackcompose

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.musicbrainz.Artist
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService

internal class MainViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    val totalFoundResults = mutableStateOf(0)

    val artists = mutableStateListOf<Artist>()

    fun queryArtists(queryString: String) {
        viewModelScope.launch {
            val foundArtists = musicBrainzApiService.queryArtists(queryString)

            totalFoundResults.value = foundArtists.count
            Log.d("debug", "queryArtists: $foundArtists")
            Log.d("debug", "count=${foundArtists.count} .size=${foundArtists.artists.size}")
            artists.clear()
            artists.addAll(foundArtists.artists)
        }
    }
}
