package ly.david.musicsearch.data.repository.release

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.release.ObserveCountOfEachStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatusCount

class ObserveCountOfEachStatusImpl(
    private val releaseDao: ReleaseDao,
) : ObserveCountOfEachStatus {
    override fun invoke(browseMethod: BrowseMethod): Flow<List<ReleaseStatusCount>> {
        return releaseDao.observeCountOfEachStatus(
            browseMethod = browseMethod,
        )
    }
}
