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
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.resourceUriPlural
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.collection.CollectionWithEntities

@HiltViewModel
internal class TopLevelViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApiService: MusicBrainzApiService
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

    fun addToCollection(collectionId: Long, entityId: String) {
        viewModelScope.launch {
            collectionEntityDao.withTransaction {
                val insertedOneEntry = collectionEntityDao.insert(
                    CollectionEntityRoomModel(
                        id = collectionId,
                        entityId = entityId
                    )
                )

                // TODO: if we insert into remote collection, then refresh collection, we lose our collection_entity
                //  which increments the collection despite it not adding to MB
                //  MB returns 200 even if entity already exists in collection
                //  Possible workaround: do not manually increment for remote collection -> but would need to requery it for its count
                //      or we can fetch all of the collection's content before inserting -> could take a long time if its large
                //  For remote collections, we could not show its count until we fetch its content? strange ux
                if (insertedOneEntry != INSERTION_FAILED_DUE_TO_CONFLICT) {
                    val collection = collectionDao.getCollection(collectionId)
                    val collectionMBid = collection.mbid
                    if (collectionMBid != null) {
                        musicBrainzApiService.uploadToCollection(
                            collectionId = collectionMBid,
                            resourceUriPlural = entity.value.resourceUriPlural,
                            mbids = entityId
                        )
                    }
                    collectionDao.update(
                        collection.copy(entityCount = collection.entityCount + 1)
                    )
                }
            }
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
