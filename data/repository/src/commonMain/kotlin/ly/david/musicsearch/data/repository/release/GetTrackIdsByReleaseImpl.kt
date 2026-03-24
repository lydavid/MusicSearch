package ly.david.musicsearch.data.repository.release

import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.release.usecase.GetTrackIdsByRelease

class GetTrackIdsByReleaseImpl(
    private val trackDao: TrackDao,
) : GetTrackIdsByRelease {
    override fun invoke(releaseId: String): List<SelectableId> {
        return trackDao.getAllTrackIdsByRelease(
            releaseId = releaseId,
        )
    }
}
