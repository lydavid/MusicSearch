package ly.david.mbjc.ui.area.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
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
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        releaseCountryDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseCountry(
                    releaseId = release.id,
                    countryId = entityId,
                    date = release.date
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseCountryDao.withTransaction {
            releaseCountryDao.deleteReleasesByCountry(entityId)
            releaseCountryDao.deleteArtistReleaseLinks(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseCountryDao.getReleasesByCountry(entityId)
        }
        else -> {
            releaseCountryDao.getReleasesByCountryFiltered(
                areaId = entityId,
                query = "%$query%"
            )
        }
    }
}
