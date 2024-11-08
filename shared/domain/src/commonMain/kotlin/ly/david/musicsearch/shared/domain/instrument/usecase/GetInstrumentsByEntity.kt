package ly.david.musicsearch.shared.domain.instrument.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.instrument.InstrumentsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetInstrumentsByEntity(
    private val instrumentsByEntityRepository: InstrumentsByEntityRepository,
) : GetEntitiesByEntity<InstrumentListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> instrumentsByEntityRepository.observeInstrumentsByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
        }
    }
}
