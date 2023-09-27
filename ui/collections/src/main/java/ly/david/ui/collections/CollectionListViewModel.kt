package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.browse.GetBrowseEntityCountUseCase
import ly.david.data.musicbrainz.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.musicbrainz.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.ui.settings.AppPreferences
import lydavidmusicsearchdatadatabase.Browse_entity_count
import lydavidmusicsearchdatadatabase.Collection
import org.koin.android.annotation.KoinViewModel

private const val ONLY_GIVE_ME_LOCAL_COLLECTIONS = "ONLY_GIVE_ME_LOCAL_COLLECTIONS"

@KoinViewModel
class CollectionListViewModel(
    val appPreferences: AppPreferences,
    private val pagedList: CollectionPagedList,
    private val musicBrainzApi: MusicBrainzApi,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val collectionDao: CollectionDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val getBrowseEntityCountUseCase: GetBrowseEntityCountUseCase,
) : ViewModel(),
    ICollectionPagedList by pagedList,
    BrowseCollectionUseCase<Collection> {

    init {
        pagedList.scope = viewModelScope
        pagedList.useCase = this
    }

    fun getUsernameThenLoadCollections() {
        viewModelScope.launch {
            // Hack: We're using username in place of a UUID
            musicBrainzAuthStore.username.collectLatest { username ->
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
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.COLLECTION,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count
                )
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.COLLECTION,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAllRemote(collectionMusicBrainzModels)

        return collectionMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return getBrowseEntityCountUseCase(entityId, MusicBrainzEntity.COLLECTION)?.remoteCount
    }

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return getBrowseEntityCountUseCase(entityId, MusicBrainzEntity.COLLECTION)?.localCount ?: 0
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return

        browseEntityCountDao.deleteAllBrowseEntityCountByRemoteCollections()
        collectionDao.deleteMusicBrainzCollections()
    }

    override fun getLinkedEntitiesPagingSource(
        viewState: ICollectionPagedList.ViewModelState,
    ): PagingSource<Int, Collection> =
        collectionDao.getAllCollections(
            showLocal = viewState.showLocal,
            showRemote = viewState.showRemote,
            query = "%${viewState.query}%"
        )
}
