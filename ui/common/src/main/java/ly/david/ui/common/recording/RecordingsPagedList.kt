package ly.david.ui.common.recording

import ly.david.data.core.RecordingWithArtistCredits
import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class RecordingsPagedList : PagedList<RecordingWithArtistCredits, RecordingListItemModel>()
