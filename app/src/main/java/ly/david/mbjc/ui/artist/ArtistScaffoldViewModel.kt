package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.collection.CollectionWithEntities
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.ArtistRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList
import ly.david.mbjc.ui.settings.AppPreferences

@HiltViewModel
internal class ArtistScaffoldViewModel @Inject constructor(
    private val repository: ArtistRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    private val collectionDao: CollectionDao,
    val appPreferences: AppPreferences
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.ARTIST
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val artist: MutableStateFlow<ArtistListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        artist.flatMapLatest {
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    collectionDao.getAllCollectionsOfType(resource)
                }
            ).flow.map { pagingData ->
                pagingData.map { collection: CollectionWithEntities ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun loadDataForTab(
        artistId: String,
        selectedTab: ArtistTab
    ) {
        when (selectedTab) {
            ArtistTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val artistListItemModel = repository.lookupArtist(artistId)
                        if (title.value.isEmpty()) {
                            title.value = artistListItemModel.getNameWithDisambiguation()
                        }
                        artist.value = artistListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
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

    fun addToCollection(collectionId: Long, artistId: String) {
        viewModelScope.launch {
            collectionDao.insertEntityIntoCollection(
                CollectionEntityRoomModel(collectionId, artistId)
            )
        }
    }

    suspend fun createNewCollection(name: String, entity: MusicBrainzResource) {
        collectionDao.insert(
            CollectionRoomModel(
            name = name,
            entity = entity
        )
        )
    }
}
