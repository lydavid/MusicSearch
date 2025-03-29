package ly.david.musicsearch.shared.domain.releasegroup

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface ReleaseGroupsByEntityRepository {
    fun observeReleaseGroupsByEntity(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>
}
