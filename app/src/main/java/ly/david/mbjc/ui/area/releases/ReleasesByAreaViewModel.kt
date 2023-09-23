package ly.david.mbjc.ui.area.releases

import androidx.paging.PagingSource
import ly.david.data.core.ReleaseForListItem
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByAreaViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseCountryDao: ReleaseCountryDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    releaseDao: ReleaseDao,
    pagedList: ReleasesPagedList,
) : ReleasesByEntityViewModel(
    browseEntityCountDao = browseEntityCountDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByArea(
            areaId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        releaseCountryDao.insertAll(
            areaId = entityId,
            releases = releaseMusicBrainzModels,
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseCountryDao.withTransaction {
            releaseCountryDao.deleteReleasesByCountry(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> =
        releaseCountryDao.getReleasesByCountry(
            areaId = entityId,
            query = "%$query%",
        )
}
