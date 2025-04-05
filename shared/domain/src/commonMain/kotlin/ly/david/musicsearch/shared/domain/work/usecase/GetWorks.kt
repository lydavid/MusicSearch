package ly.david.musicsearch.shared.domain.work.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.work.WorksByEntityRepository

class GetWorks(
    private val worksByEntityRepository: WorksByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<WorkListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<WorkListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            worksByEntityRepository.observeWorksByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
