package ly.david.mbjc.ui.recording.releases

import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByRecordingViewModel(
    getReleasesByEntity: GetReleasesByEntity,
) : EntitiesByEntityViewModel<ReleaseListItemModel>(
    entity = MusicBrainzEntity.RECORDING,
    getEntitiesByEntity = getReleasesByEntity,
)
