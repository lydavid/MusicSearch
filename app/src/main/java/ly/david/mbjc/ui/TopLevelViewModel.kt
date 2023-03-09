package ly.david.mbjc.ui

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
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.collection.CollectionWithEntities

@HiltViewModel
internal class TopLevelViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
) : ViewModel() {

    val entity: MutableStateFlow<MusicBrainzResource> = MutableStateFlow(MusicBrainzResource.ARTIST)

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        entity.flatMapLatest {
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    collectionDao.getAllCollectionsOfType(it)
                }
            ).flow.map { pagingData ->
                pagingData.map { collection: CollectionWithEntities ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun setEntity(entity: MusicBrainzResource) {
        this.entity.value = entity
    }

    fun addToCollection(collectionId: Long, artistId: String) {
        viewModelScope.launch {
            collectionEntityDao.insert(
                CollectionEntityRoomModel(
                    id = collectionId,
                    entityId = artistId
                )
            )
        }
    }

    fun createNewCollection(name: String, entity: MusicBrainzResource) {
        viewModelScope.launch {
            collectionDao.insert(
                CollectionRoomModel(
                    name = name,
                    entity = entity
                )
            )
        }
    }
}
