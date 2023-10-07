package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.spotify.di.ArtistImageRepository
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.artist.ArtistScaffoldModel
import ly.david.musicsearch.domain.history.IncrementLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class ArtistScaffoldViewModel(
    private val repository: ArtistRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsList: RelationsList,
    private val artistImageRepository: ArtistImageRepository,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.ARTIST
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val artist: MutableStateFlow<ArtistScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
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
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = artistId,
                                title = title.value,
                                entity = entity,
                                searchHint = artist.value?.sortName ?: "",
                            )
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
            artistImageRepository.getArtistImageFromNetwork(artist.id, spotifyUrl)
        } else {
            imageUrl
        }
    }
}
