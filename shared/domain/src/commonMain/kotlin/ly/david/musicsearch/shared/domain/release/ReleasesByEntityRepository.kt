package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface ReleasesByEntityRepository {
    fun observeReleasesByEntity(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>>
}
