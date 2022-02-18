package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService

internal class SearchViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    // TODO: not that useful, could just use artists.count, unless we have it track the total number including ones not in current paging
    // right now it's max 25
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
