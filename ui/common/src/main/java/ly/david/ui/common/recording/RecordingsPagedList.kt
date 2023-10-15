package ly.david.ui.common.recording

import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class RecordingsPagedList : PagedList<RecordingListItemModel, RecordingListItemModel>()
