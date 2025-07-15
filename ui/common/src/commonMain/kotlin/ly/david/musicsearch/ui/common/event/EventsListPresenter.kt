package ly.david.musicsearch.ui.common.event

import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.list.BaseListPresenter

class EventsListPresenter(
    getEntities: GetEntities,
    observeLocalCount: ObserveLocalCount,
    appPreferences: AppPreferences,
    musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : BaseListPresenter(
    getEntities,
    observeLocalCount,
    appPreferences,
    musicBrainzImageMetadataRepository,
) {
    override fun getEntityType(): MusicBrainzEntity {
        return MusicBrainzEntity.EVENT
    }
}
