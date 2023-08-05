package ly.david.data.domain.releasegroup

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.relation.HasUrls
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel
import ly.david.data.room.releasegroup.ReleaseGroupDao

@Singleton
class ReleaseGroupRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val relationDao: RelationDao,
) : RelationsListRepository {

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val roomReleaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
        if (roomReleaseGroup != null) {
            return roomReleaseGroup.toReleaseGroupScaffoldModel()
        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
        releaseGroupDao.withTransaction {
            releaseGroupDao.insertReleaseGroupWithArtistCredits(releaseGroupMusicBrainzModel)

            val relations = mutableListOf<RelationRoomModel>()
            releaseGroupMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    entityId = releaseGroupId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)
            relationDao.markEntityHasUrls(
                hasUrls = HasUrls(
                    entityId = releaseGroupId,
                    hasUrls = true
                )
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
