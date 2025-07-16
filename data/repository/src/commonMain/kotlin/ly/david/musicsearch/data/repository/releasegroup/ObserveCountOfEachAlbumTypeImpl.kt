package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount

class ObserveCountOfEachAlbumTypeImpl(
    private val releaseGroupDao: ReleaseGroupDao,
) : ObserveCountOfEachAlbumType {
    override fun invoke(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseGroupTypeCount>> {
        return releaseGroupDao.observeCountOfEachAlbumType(
            browseMethod = browseMethod,
        )
    }
}
