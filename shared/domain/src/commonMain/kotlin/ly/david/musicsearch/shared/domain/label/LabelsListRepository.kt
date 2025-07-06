package ly.david.musicsearch.shared.domain.label

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel

interface LabelsListRepository {
    fun observeLabels(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>>

    fun observeCountOfLabels(browseMethod: BrowseMethod?): Flow<Int>
}
