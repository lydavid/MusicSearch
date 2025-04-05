package ly.david.musicsearch.shared.domain.area.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.AreasByEntityRepository
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel

interface GetAreas {
    operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>>
}

class GetAreasImpl(
    private val areasByEntityRepository: AreasByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<AreaListItemModel>, GetAreas {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<AreaListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            areasByEntityRepository.observeAreasByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
