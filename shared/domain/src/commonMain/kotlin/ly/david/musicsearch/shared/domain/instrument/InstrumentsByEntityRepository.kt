package ly.david.musicsearch.shared.domain.instrument

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

interface InstrumentsByEntityRepository {
    fun observeInstrumentsByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>>

    fun observeCountOfAllInstruments(): Flow<Long>
}
