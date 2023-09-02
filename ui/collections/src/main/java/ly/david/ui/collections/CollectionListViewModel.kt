package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.network.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.collection.CollectionWithEntities
import ly.david.data.room.collection.toCollectionRoomModel
import ly.david.data.room.relation.BrowseEntityCount
import ly.david.data.room.relation.RelationDao
import ly.david.ui.settings.AppPreferences

private const val ONLY_GIVE_ME_LOCAL_COLLECTIONS = "ONLY_GIVE_ME_LOCAL_COLLECTIONS"

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    val appPreferences: AppPreferences,
    private val pagedList: CollectionPagedList,
    private val musicBrainzApi: MusicBrainzApi,
    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val collectionDao: CollectionDao,
    private val relationDao: RelationDao,
) : ViewModel(),
    ICollectionPagedList by pagedList,
    BrowseCollectionUseCase<CollectionWithEntities> {

    init {
        pagedList.scope = viewModelScope
        pagedList.useCase = this
    }

    fun getUsernameThenLoadCollections() {
        viewModelScope.launch {
            // Hack: We're using username in place of a UUID
            musicBrainzAuthState.username.collectLatest { username ->
                if (username.isEmpty()) {
                    // This id lets the rest of the functions know to avoid network requests
                    loadPagedEntities(ONLY_GIVE_ME_LOCAL_COLLECTIONS)
                } else {
                    loadPagedEntities(username)
                }
            }
        }
    }

    fun setShowLocal(show: Boolean) {
        appPreferences.setShowLocalCollections(show)
        updateShowLocal(show)
    }

    fun setShowRemote(show: Boolean) {
        appPreferences.setShowRemoteCollections(show)
        updateShowRemote(show)
    }

    override suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        val response = musicBrainzApi.browseCollectionsByUser(
            username = entityId,
            offset = nextOffset,
            include = USER_COLLECTIONS
        )

        if (response.offset == 0) {
            relationDao.insertBrowseEntityCount(
                browseEntityCount = BrowseEntityCount(
                    entityId = entityId,
                    browseEntity = MusicBrainzEntity.COLLECTION,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.COLLECTION,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAll(collectionMusicBrainzModels.map { it.toCollectionRoomModel() })

        return collectionMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.COLLECTION)?.remoteCount
    }

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.COLLECTION)?.localCount ?: 0
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return

        collectionDao.withTransaction {
            relationDao.deleteAllBrowseEntityCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    override fun getLinkedEntitiesPagingSource(
        viewState: ICollectionPagedList.ViewModelState,
    ): PagingSource<Int, CollectionWithEntities> =
        collectionDao.getAllCollections(
            showLocal = viewState.showLocal,
            showRemote = viewState.showRemote,
            query = "%${viewState.query}%"
        )
}
