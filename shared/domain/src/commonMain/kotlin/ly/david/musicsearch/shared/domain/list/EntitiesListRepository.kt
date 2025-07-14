package ly.david.musicsearch.shared.domain.list

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface EntitiesListRepository {
    fun observeEntities(
        entity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>

    fun observeLocalCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int>

    fun observeVisitedCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int?>
}
