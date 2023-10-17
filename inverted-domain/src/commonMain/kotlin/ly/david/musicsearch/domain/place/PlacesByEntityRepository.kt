package ly.david.musicsearch.domain.place

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface PlacesByEntityRepository {
    fun observePlacesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<PlaceListItemModel>>
}
