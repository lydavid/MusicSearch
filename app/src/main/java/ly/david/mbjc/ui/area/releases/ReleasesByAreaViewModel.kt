package ly.david.mbjc.ui.area.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.paging.PagedList
import ly.david.ui.common.release.ReleasesByEntityViewModel

@HiltViewModel
internal class ReleasesByAreaViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseCountryDao: ReleaseCountryDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
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
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
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
