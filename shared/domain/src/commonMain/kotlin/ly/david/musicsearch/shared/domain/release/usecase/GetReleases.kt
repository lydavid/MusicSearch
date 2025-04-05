package ly.david.musicsearch.shared.domain.release.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository

class GetReleases(
    private val releasesByEntityRepository: ReleasesByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<ReleaseListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            releasesByEntityRepository.observeReleasesByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
