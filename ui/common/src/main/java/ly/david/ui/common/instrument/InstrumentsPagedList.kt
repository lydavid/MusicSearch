package ly.david.ui.common.instrument

import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.data.room.instrument.InstrumentRoomModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class InstrumentsPagedList : PagedList<InstrumentRoomModel, InstrumentListItemModel>()
