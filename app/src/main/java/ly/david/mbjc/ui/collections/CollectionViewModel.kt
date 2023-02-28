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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionRoomModel

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao
) : ViewModel() {

    private val query: MutableStateFlow<String> = MutableStateFlow("")

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
                pagingData.map { collection: CollectionRoomModel ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    suspend fun createNewCollection(name: String, entity: MusicBrainzResource) {
        collectionDao.insert(CollectionRoomModel(
            name = name,
            entity = entity
        ))
    }
}
