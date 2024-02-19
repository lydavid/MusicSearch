package ly.david.mbjc.ui.place.events

import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.event.usecase.GetEventsByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EventsByPlaceViewModel(
    getEventsByEntity: GetEventsByEntity,
) : EntitiesByEntityViewModel<EventListItemModel>(
    entity = MusicBrainzEntity.PLACE,
    getEntitiesByEntity = getEventsByEntity,
)
