package ly.david.musicsearch.ui.common.releasegroup

import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.list.BaseListPresenter

class ReleaseGroupsListPresenter(
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
    override fun getEntityType(): MusicBrainzEntityType {
        return MusicBrainzEntityType.RELEASE_GROUP
    }
}
