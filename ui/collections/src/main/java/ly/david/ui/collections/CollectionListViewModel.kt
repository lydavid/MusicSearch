package ly.david.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ly.david.data.musicbrainz.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.musicbrainz.auth.MusicBrainzAuthStore
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.domain.browse.usecase.GetBrowseEntityCountUseCase
import ly.david.musicsearch.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.domain.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import lydavidmusicsearchdatadatabase.Browse_entity_count
import org.koin.android.annotation.KoinViewModel

private const val ONLY_GIVE_ME_LOCAL_COLLECTIONS = "ONLY_GIVE_ME_LOCAL_COLLECTIONS"

@KoinViewModel
class CollectionListViewModel(
    val appPreferences: AppPreferences,
    private val musicBrainzApi: MusicBrainzApi,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val collectionDao: CollectionDao,
    private val getAllCollections: GetAllCollections,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val getBrowseEntityCountUseCase: GetBrowseEntityCountUseCase,
) : ViewModel() {

    data class ViewModelState(
        val entityId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val showLocal: Boolean,
        val showRemote: Boolean,
    )

    val entityId: MutableStateFlow<String> = MutableStateFlow("")
    val query: MutableStateFlow<String> = MutableStateFlow("")
    val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val showLocal: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val showRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(
        entityId,
        query,
        isRemote,
        showLocal,
        showRemote,
    ) { entityId, query, isRemote, showLocal, showRemote ->
        ViewModelState(
            entityId,
            query,
            isRemote,
            showLocal,
            showRemote,
        )
    }.distinctUntilChanged()

    @OptIn(
        ExperimentalPagingApi::class,
        ExperimentalCoroutinesApi::class,
    )
    val pagedEntities: Flow<PagingData<CollectionListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { state ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(state.entityId).takeIf { state.isRemote },
                    pagingSourceFactory = { getLinkedEntitiesPagingSource(state) },
                ).flow
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }

    private fun getRemoteMediator(entityId: String) = BrowseEntityRemoteMediator<CollectionListItemModel>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = { deleteLinkedEntitiesByEntity(entityId) },
        browseEntity = { offset ->
            browseLinkedEntitiesAndStore(
                entityId,
                offset,
            )
        },
    )

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun getUsernameThenLoadCollections() {
        viewModelScope.launch {
            // Hack: We're using username in place of a UUID
            musicBrainzAuthStore.username.collectLatest { username ->
                if (username.isEmpty()) {
                    // This id lets the rest of the functions know to avoid network requests
                    entityId.value = ONLY_GIVE_ME_LOCAL_COLLECTIONS
                } else {
                    entityId.value = username
                }
            }
        }
    }

    fun setShowLocal(show: Boolean) {
        appPreferences.setShowLocalCollections(show)
        showLocal.value = show
    }

    fun setShowRemote(show: Boolean) {
        appPreferences.setShowRemoteCollections(show)
        showRemote.value = show
    }

    private suspend fun browseLinkedEntitiesAndStore(
        entityId: String,
        nextOffset: Int,
    ): Int {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        val response = musicBrainzApi.browseCollectionsByUser(
            username = entityId,
            offset = nextOffset,
            include = USER_COLLECTIONS,
        )

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.COLLECTION,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count,
                ),
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.COLLECTION,
                additionalOffset = response.musicBrainzModels.size,
            )
        }

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAllRemote(collectionMusicBrainzModels)

        return collectionMusicBrainzModels.size
    }

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return getBrowseEntityCountUseCase(
            entityId,
            MusicBrainzEntity.COLLECTION,
        )?.remoteCount
    }

    private fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return getBrowseEntityCountUseCase(
            entityId,
            MusicBrainzEntity.COLLECTION,
        )?.localCount ?: 0
    }

    private fun deleteLinkedEntitiesByEntity(entityId: String) {
        if (entityId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return

        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteAllBrowseEntityCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    private fun getLinkedEntitiesPagingSource(
        viewState: ViewModelState,
    ): PagingSource<Int, CollectionListItemModel> =
        collectionDao.getAllCollections(
            showLocal = viewState.showLocal,
            showRemote = viewState.showRemote,
            query = "%${viewState.query}%",
        )
}
