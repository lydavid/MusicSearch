package ly.david.musicsearch.domain.label

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface LabelsByEntityRepository {
    fun observeLabelsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>>
}
