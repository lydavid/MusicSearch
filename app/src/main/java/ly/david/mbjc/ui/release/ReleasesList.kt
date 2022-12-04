package ly.david.mbjc.ui.release

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
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.repository.ReleasesListRepository

internal interface IReleasesList {
    data class ViewModelState(
        val resourceId: String = "",
        val query: String = ""
    )

    val resourceId: MutableStateFlow<String>
    val query: MutableStateFlow<String>
    val paramState: Flow<ViewModelState>

    fun loadReleases(resourceId: String) {
        this.resourceId.value = resourceId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    val pagedReleases: Flow<PagingData<ReleaseListItemModel>>
}

/**
 * Generic implementation for handling paged releases.
 *
 * Meant to be implemented by a ViewModel through delegation.
 *
 * The ViewModel should should assign [scope] and [repository] in its init block.
 */
internal class ReleasesList @Inject constructor() : IReleasesList {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val paramState = combine(resourceId, query) { resourceId, query ->
        IReleasesList.ViewModelState(resourceId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var repository: ReleasesListRepository

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedReleases: Flow<PagingData<ReleaseListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getRemoteReleasesCountByResource(resourceId) },
                        getLocalResourceCount = { repository.getLocalReleasesCountByResource(resourceId) },
                        deleteLocalResource = { repository.deleteReleasesByResource(resourceId) },
                        browseResource = { offset ->
                            repository.browseReleasesAndStore(resourceId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getReleasesPagingSource(resourceId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
