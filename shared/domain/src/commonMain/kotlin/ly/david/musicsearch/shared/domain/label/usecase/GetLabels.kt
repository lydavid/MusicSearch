package ly.david.musicsearch.shared.domain.label.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

class GetLabels(
    private val labelsByEntityRepository: LabelsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<LabelListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            labelsByEntityRepository.observeLabelsByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
