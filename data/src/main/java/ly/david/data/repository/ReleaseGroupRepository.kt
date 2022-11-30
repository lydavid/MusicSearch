package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.domain.toUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
) : ReleasesListRepository, RelationsListRepository {

    // We need ReleaseGroupUiModel so that we have artist credits
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel {
        val roomReleaseGroup = releaseGroupDao.getReleaseGroupWithArtistCredits(releaseGroupId)
        if (roomReleaseGroup != null) {
            return roomReleaseGroup.toUiModel()
        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
        releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroupMusicBrainzModel)
        return releaseGroupMusicBrainzModel.toUiModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }

    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertOrUpdate(musicBrainzReleases.map { it.toReleaseRoomModel(resourceId) })

        return musicBrainzReleases.size
    }

    override suspend fun getRemoteReleasesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteReleasesByResource(resourceId: String) {
        releasesReleaseGroupsDao.deleteReleasesByReleaseGroup(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getReleasesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroup(resourceId)
        }
        else -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = resourceId,
                query = "%$query%"
            )
        }
    }
}
