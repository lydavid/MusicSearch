package ly.david.musicsearch.shared.domain.instrument

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

interface InstrumentsListRepository {
    fun observeInstruments(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>>
}
