package ly.david.musicsearch.shared.domain.instrument.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.instrument.InstrumentsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

class GetInstruments(
    private val instrumentsByEntityRepository: InstrumentsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<InstrumentListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            instrumentsByEntityRepository.observeInstrumentsByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
