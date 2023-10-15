package ly.david.ui.common.label

import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.ui.common.paging.PagedList
import org.koin.core.annotation.Factory

@Factory
class LabelsPagedList : PagedList<LabelListItemModel>()
