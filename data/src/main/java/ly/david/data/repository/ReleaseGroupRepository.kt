package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.domain.toReleaseGroupListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.releasegroup.ReleaseGroupDao

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
) : RelationsListRepository {

    // We need ReleaseGroupUiModel so that we have artist credits
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupListItemModel {
        val roomReleaseGroup = releaseGroupDao.getReleaseGroupWithArtistCredits(releaseGroupId)
        if (roomReleaseGroup != null) {
            return roomReleaseGroup.toReleaseGroupListItemModel()
        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
        releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroupMusicBrainzModel)
        return releaseGroupMusicBrainzModel.toReleaseGroupListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
