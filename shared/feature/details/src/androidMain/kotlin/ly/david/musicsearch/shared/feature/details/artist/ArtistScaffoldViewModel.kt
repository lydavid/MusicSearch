package ly.david.musicsearch.shared.feature.details.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.spotify.ArtistImageRepository
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArtistScaffoldViewModel(
    private val repository: ArtistRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsList: RelationsList,
    private val artistImageRepository: ArtistImageRepository,
    private val logger: Logger,
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
        relationsList.entity = entity
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
                        logger.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = artistId,
                                title = title.value,
                                entity = entity,
                                searchHint = artist.value?.sortName ?: "",
                            ),
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