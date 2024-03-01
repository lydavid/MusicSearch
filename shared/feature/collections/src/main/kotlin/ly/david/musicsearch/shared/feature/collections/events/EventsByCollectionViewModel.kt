package ly.david.musicsearch.shared.feature.collections.events

import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.event.usecase.GetEventsByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EventsByCollectionViewModel(
    getEventsByEntity: GetEventsByEntity,
) : EntitiesByEntityViewModel<EventListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getEventsByEntity,
)
