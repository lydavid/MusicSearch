package ly.david.musicsearch.shared.domain.place

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface PlacesByEntityRepository {
    fun observePlacesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters = ListFilters(),
    ): Flow<PagingData<PlaceListItemModel>>
}
