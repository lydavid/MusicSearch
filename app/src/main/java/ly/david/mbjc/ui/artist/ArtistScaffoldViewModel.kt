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
import ly.david.data.image.ImageUrlSaver
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.data.spotify.ArtistImageManager
import ly.david.data.spotify.SpotifyApi
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class ArtistScaffoldViewModel @Inject constructor(
    private val repository: ArtistRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    override val spotifyApi: SpotifyApi,
    override val imageUrlSaver: ImageUrlSaver,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList,
    ArtistImageManager {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.ARTIST
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
        selectedTab: ArtistTab,
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
                        fetchArtistImage(artistScaffoldModel)
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
                            entityId = artistId,
                            entity = entity,
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

    private suspend fun fetchArtistImage(
        artist: ArtistScaffoldModel,
    ) {
        val imageUrl = artist.imageUrl
        url.value = if (imageUrl == null) {
            val spotifyUrl =
                artist.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name ?: return
            getArtistImageFromNetwork(artist.id, spotifyUrl)
        } else {
            imageUrl
        }
    }
}
