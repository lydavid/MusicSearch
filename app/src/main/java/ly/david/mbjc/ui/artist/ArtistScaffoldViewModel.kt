package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.artist.ArtistRepository
import ly.david.data.domain.artist.ArtistScaffoldModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.data.spotify.SpotifyApi
import ly.david.data.spotify.getLargeImageUrl
import ly.david.ui.common.MusicBrainzResourceViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class ArtistScaffoldViewModel @Inject constructor(
    private val repository: ArtistRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    private val spotifyApi: SpotifyApi
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.ARTIST
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val artist: MutableStateFlow<ArtistScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
        artistId: String,
        selectedTab: ArtistTab
    ) {
        when (selectedTab) {
            ArtistTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val artistScaffoldModel = repository.lookupArtist(artistId)
                        if (title.value.isEmpty()) {
                            title.value = artistScaffoldModel.getNameWithDisambiguation()
                        }
                        artist.value = artistScaffoldModel
                        getCoverArtUrl(artistScaffoldModel)
                        isError.value = false
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = artistId,
                            resource = resource,
                            summary = title.value,
                            searchHint = artist.value?.sortName ?: ""
                        )
                        recordedLookup = true
                    }
                }
            }
            ArtistTab.RELATIONSHIPS -> loadRelations(artistId)
            else -> {
                // Not handled here.
            }
        }
    }

    private suspend fun getCoverArtUrl(
        artist: ArtistScaffoldModel
    ) {
        val spotifyUrl = artist.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name ?: return
        val spotifyArtistId = spotifyUrl.split("/").last()

        url.value = spotifyApi.getArtist(
            artistId = spotifyArtistId,
        ).getLargeImageUrl()
    }
}
