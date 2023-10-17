package ly.david.ui.collections.instruments

import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.instrument.usecase.GetInstrumentsByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class InstrumentsByCollectionViewModel(
    getInstrumentsByEntity: GetInstrumentsByEntity,
) : EntitiesByEntityViewModel<InstrumentListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getInstrumentsByEntity,
)
