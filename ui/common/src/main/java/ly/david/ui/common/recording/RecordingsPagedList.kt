package ly.david.ui.common.recording

import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.data.room.recording.RecordingForListItem
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class RecordingsPagedList: PagedList<RecordingForListItem, RecordingListItemModel>()
