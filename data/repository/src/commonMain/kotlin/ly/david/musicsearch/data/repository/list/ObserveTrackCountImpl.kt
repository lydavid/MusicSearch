package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.shared.domain.list.ObserveTrackCount

class ObserveTrackCountImpl(
    private val trackDao: TrackDao,
) : ObserveTrackCount {
    override fun invoke(releaseId: String): Flow<Int> {
        return trackDao.observeCountOfTracksByRelease(releaseId = releaseId)
    }
}
