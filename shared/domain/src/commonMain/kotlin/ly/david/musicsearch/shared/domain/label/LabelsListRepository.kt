package ly.david.musicsearch.shared.domain.label

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

interface LabelsListRepository {
    fun observeLabels(
        browseMethod: BrowseMethod,
        listFilters: ListFilters.Labels,
    ): Flow<PagingData<LabelListItemModel>>
}
