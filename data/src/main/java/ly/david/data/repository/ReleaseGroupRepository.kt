package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.domain.toReleaseGroupUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.getReleaseGroupArtistCreditRoomModels
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.data.persistence.releasegroup.toReleaseGroupRoomModel

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
) {

    // We need ReleaseGroupUiModel so that we have artist credits
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel {
        val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
        if (roomReleaseGroup != null) {
            return roomReleaseGroup.toReleaseGroupUiModel(
                releaseGroupArtistDao.getReleaseGroupArtistCredits(
                    releaseGroupId
                )
            )
        }

        val musicBrainzReleaseGroup = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)

        // Whenever we insert a release group, we must always insert all of its artists as well
        releaseGroupDao.insert(musicBrainzReleaseGroup.toReleaseGroupRoomModel())
        releaseGroupArtistDao.insertAll(musicBrainzReleaseGroup.getReleaseGroupArtistCreditRoomModels())

        return musicBrainzReleaseGroup.toReleaseGroupUiModel()
    }

    suspend fun browseReleasesAndStore(releaseGroupId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = releaseGroupId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(releaseGroupId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel(releaseGroupId) })

        return musicBrainzReleases.size
    }

    suspend fun getRemoteReleasesByReleaseGroupCount(releaseGroupId: String): Int? =
        relationDao.getBrowseResourceOffset(releaseGroupId, MusicBrainzResource.RELEASE)?.remoteCount

    suspend fun getLocalReleasesByReleaseGroupCount(releaseGroupId: String) =
        relationDao.getBrowseResourceOffset(releaseGroupId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    suspend fun deleteReleasesByReleaseGroup(releaseGroupId: String) {
        releasesReleaseGroupsDao.deleteReleasesInReleaseGroup(releaseGroupId)
        relationDao.deleteBrowseResourceOffsetByResource(releaseGroupId, MusicBrainzResource.RELEASE)
    }

    fun getPagingSource(releaseGroupId: String, query: String): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroup(releaseGroupId)
        }
        else -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = releaseGroupId,
                query = "%$query%"
            )
        }
    }
}
