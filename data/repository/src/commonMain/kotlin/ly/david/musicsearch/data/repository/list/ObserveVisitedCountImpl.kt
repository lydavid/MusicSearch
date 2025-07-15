package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ObserveVisitedCountImpl(
    private val releaseGroupDao: ReleaseGroupDao,
) : ObserveVisitedCount {
    override fun invoke(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int?> {
        if (browseMethod == null) return flowOf(0)
        return when (browseEntity) {
            MusicBrainzEntity.AREA -> flowOf(null)
            MusicBrainzEntity.ARTIST -> flowOf(null)
            MusicBrainzEntity.EVENT -> flowOf(null)
            MusicBrainzEntity.GENRE -> flowOf(null)
            MusicBrainzEntity.INSTRUMENT -> flowOf(null)
            MusicBrainzEntity.LABEL -> flowOf(null)
            MusicBrainzEntity.PLACE -> flowOf(null)
            MusicBrainzEntity.RECORDING -> flowOf(null)
            MusicBrainzEntity.RELEASE -> flowOf(null)

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupDao.observeVisitedCount(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.SERIES -> flowOf(null)
            MusicBrainzEntity.WORK -> flowOf(null)

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.URL,
            -> flowOf(0)
        }
    }
}
