package ly.david.mbjc.ui.collections

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionWithEntities
import timber.log.Timber

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val musicBrainzAuthState: MusicBrainzAuthState
) : ViewModel() {

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            try {
                musicBrainzAuthState.username.collectLatest {
                    val collections =
                        musicBrainzApiService.getAllCollectionsByUser(
                            username = it,
                            include = USER_COLLECTIONS
                        )
                    Timber.d("${collections.collections}")
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        query.flatMapLatest { query ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    if (query.isEmpty()) {
                        collectionDao.getAllCollections()
                    } else {
                        collectionDao.getAllCollectionsFiltered("%$query%")
                    }
                }
            ).flow.map { pagingData ->
                pagingData.map { collection: CollectionWithEntities ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
