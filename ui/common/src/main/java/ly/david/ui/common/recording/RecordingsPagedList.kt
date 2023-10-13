package ly.david.ui.common.recording

import ly.david.musicsearch.data.core.RecordingForListItem
import ly.david.musicsearch.data.core.listitem.RecordingListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class RecordingsPagedList : PagedList<RecordingForListItem, RecordingListItemModel>()
