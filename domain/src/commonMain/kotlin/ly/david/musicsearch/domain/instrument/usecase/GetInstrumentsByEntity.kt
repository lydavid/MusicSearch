package ly.david.musicsearch.domain.instrument.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.domain.instrument.InstrumentsByEntityRepository
import org.koin.core.annotation.Single

@Single
class GetInstrumentsByEntity(
    private val instrumentsByEntityRepository: InstrumentsByEntityRepository,
) : GetEntitiesByEntity<InstrumentListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = instrumentsByEntityRepository.observeInstrumentsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
