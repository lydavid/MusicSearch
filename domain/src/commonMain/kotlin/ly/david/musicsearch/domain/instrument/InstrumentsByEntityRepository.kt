package ly.david.musicsearch.domain.instrument

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface InstrumentsByEntityRepository {
    fun observeInstrumentsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>>
}
