package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.VisitedStatsDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ObserveVisitedCountImpl(
    private val visitedStatsDao: VisitedStatsDao,
) : ObserveVisitedCount {
    override fun invoke(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        if (browseMethod == null) return flowOf(0)
        return visitedStatsDao.observeVisitedCount(
            browseEntity = browseEntity,
            browseMethod = browseMethod,
        )
    }
}
