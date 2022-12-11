package ly.david.mbjc.ui.place

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
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.toPlaceListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.repository.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList

internal class PlacesPagedList @Inject constructor() : PagedList<PlaceListItemModel> {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val paramState = combine(resourceId, query) { resourceId, query ->
        PagedList.ViewModelState(resourceId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseResourceUseCase<PlaceRoomModel>

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedResources: Flow<PagingData<PlaceListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { useCase.getRemoteLinkedResourcesCountByResource(resourceId) },
                        getLocalResourceCount = { useCase.getLocalLinkedResourcesCountByResource(resourceId) },
                        deleteLocalResource = { useCase.deleteLinkedResourcesByResource(resourceId) },
                        browseResource = { offset ->
                            useCase.browseLinkedResourcesAndStore(resourceId, offset)
                        }
                    ),
                    pagingSourceFactory = { useCase.getLinkedResourcesPagingSource(resourceId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toPlaceListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
