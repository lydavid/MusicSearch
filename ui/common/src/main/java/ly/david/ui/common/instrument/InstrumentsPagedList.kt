package ly.david.ui.common.instrument

import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class InstrumentsPagedList : PagedList<InstrumentListItemModel>()
