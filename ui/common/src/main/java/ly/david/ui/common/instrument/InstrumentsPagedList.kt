package ly.david.ui.common.instrument

import ly.david.musicsearch.data.core.listitem.InstrumentListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class InstrumentsPagedList : PagedList<InstrumentListItemModel, InstrumentListItemModel>()
