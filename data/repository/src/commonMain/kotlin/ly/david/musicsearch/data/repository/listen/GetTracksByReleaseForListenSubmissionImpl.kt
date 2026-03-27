package ly.david.musicsearch.data.repository.listen

import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.shared.domain.listen.GetTracksByReleaseForListenSubmission
import ly.david.musicsearch.shared.domain.listen.TrackInfo

class GetTracksByReleaseForListenSubmissionImpl(
    private val trackDao: TrackDao,
) : GetTracksByReleaseForListenSubmission {
    override fun invoke(releaseId: String): List<TrackInfo> {
        return trackDao.getTracksByReleaseForListenSubmission(
            releaseId = releaseId,
        )
    }
}
