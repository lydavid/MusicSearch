package ly.david.data.domain.releasegroup

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.releasegroup.ReleaseGroupDao

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
) : RelationsListRepository {

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
        if (roomReleaseGroup != null) {
            return roomReleaseGroup.toReleaseGroupScaffoldModel()
        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
        releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroupMusicBrainzModel)
        return lookupReleaseGroup(releaseGroupId)
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
