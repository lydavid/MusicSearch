package ly.david.mbjc.ui.releasegroup.releases

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.common.release.ReleasesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByReleaseGroupViewModel(
    getReleasesByEntity: GetReleasesByEntity,
) : ReleasesByEntityViewModel(
    entity = MusicBrainzEntity.RELEASE_GROUP,
    getReleasesByEntity = getReleasesByEntity,
)
