package ly.david.ui.common.instrument

import ly.david.musicsearch.domain.listitem.InstrumentListItemModel
import ly.david.ui.common.paging.PagedList
import lydavidmusicsearchdatadatabase.Instrument
import org.koin.core.annotation.Factory

@Factory
class InstrumentsPagedList : PagedList<Instrument, InstrumentListItemModel>()
