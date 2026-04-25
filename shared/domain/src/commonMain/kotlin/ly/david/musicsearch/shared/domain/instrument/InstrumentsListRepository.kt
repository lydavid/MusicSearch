package ly.david.musicsearch.shared.domain.instrument

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel

interface InstrumentsListRepository {
    fun observeInstruments(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>>
}
