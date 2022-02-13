package ly.david.musicbrainzjetpackcompose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService

internal class MainViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    fun queryArtists(queryString: String) {
        viewModelScope.launch {
            val artists = musicBrainzApiService.queryArtists(queryString)
            Log.d("debug", "queryArtists: $artists")
        }
    }
}
