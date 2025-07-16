package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.CollectedStatsDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ObserveCollectedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ObserveCollectedCountImpl(
    private val collectedStatsDao: CollectedStatsDao,
) : ObserveCollectedCount {
    override fun invoke(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        if (browseMethod == null) return flowOf(0)
        return collectedStatsDao.observeCollectedCount(
            browseEntity = browseEntity,
            browseMethod = browseMethod,
        )
    }
}
