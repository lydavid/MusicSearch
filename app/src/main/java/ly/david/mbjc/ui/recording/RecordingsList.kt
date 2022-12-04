package ly.david.mbjc.ui.recording

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.toListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.repository.RecordingsListRepository

internal interface IRecordingsList {
    data class ViewModelState(
        val resourceId: String = "",
        val query: String = ""
    )

    val resourceId: MutableStateFlow<String>
    val query: MutableStateFlow<String>
    val paramState: Flow<ViewModelState>

    fun loadRecordings(resourceId: String) {
        this.resourceId.value = resourceId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    val pagedRecordings: Flow<PagingData<RecordingListItemModel>>
}

/**
 * Generic implementation for handling paged recordings.
 *
 * Meant to be implemented by a ViewModel through delegation.
 *
 * The ViewModel should should assign [scope] and [repository] in its init block.
 */
internal class RecordingsList @Inject constructor() : IRecordingsList {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val paramState = combine(resourceId, query) { resourceId, query ->
        IRecordingsList.ViewModelState(resourceId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var repository: RecordingsListRepository

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedRecordings: Flow<PagingData<RecordingListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getRemoteRecordingsCountByResource(resourceId) },
                        getLocalResourceCount = { repository.getLocalRecordingsCountByResource(resourceId) },
                        deleteLocalResource = { repository.deleteRecordingsByResource(resourceId) },
                        browseResource = { offset ->
                            repository.browseRecordingsAndStore(resourceId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getRecordingsPagingSource(resourceId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
