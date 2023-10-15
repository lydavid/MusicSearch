package ly.david.mbjc.ui.label.releases

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.common.release.ReleasesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByLabelViewModel(
    getReleasesByEntity: GetReleasesByEntity,
) : ReleasesByEntityViewModel(
    entity = MusicBrainzEntity.LABEL,
    getReleasesByEntity = getReleasesByEntity,
)
