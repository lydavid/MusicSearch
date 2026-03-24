package ly.david.musicsearch.shared.domain.release.usecase

import ly.david.musicsearch.shared.domain.listitem.SelectableId

interface GetTrackIdsByRelease {
    operator fun invoke(releaseId: String): List<SelectableId>
}
