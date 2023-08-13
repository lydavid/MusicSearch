package ly.david.data.domain.releasegroup

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.releasegroup.ReleaseGroupDao

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val releaseGroupWithAllData = releaseGroupDao.getReleaseGroup(releaseGroupId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseGroupId)
        if (releaseGroupWithAllData != null && hasUrlsBeenSavedForEntity) {
            return releaseGroupWithAllData.toReleaseGroupScaffoldModel()
        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
        releaseGroupDao.withTransaction {
            releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroupMusicBrainzModel)
            relationRepository.insertAllRelations(
                entityId = releaseGroupId,
                relationMusicBrainzModels = releaseGroupMusicBrainzModel.relations,
            )
        }
        return lookupReleaseGroup(releaseGroupId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
