package ly.david.ui.collections.releasegroups

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityViewModel
import ly.david.ui.common.releasegroup.ReleaseGroupsPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RoomRelationDao,
    releaseGroupDao: ReleaseGroupDao,
    releaseGroupsPagedList: ReleaseGroupsPagedList,
) : ReleaseGroupsByEntityViewModel(
    relationDao = relationDao,
    releaseGroupDao = releaseGroupDao,
    releaseGroupsPagedList = releaseGroupsPagedList,
) {

    override suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse {
        return musicBrainzApi.browseReleaseGroupsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ) {
        collectionEntityDao.insertAll(
            releaseGroupMusicBrainzModels.map { releaseGroup ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = releaseGroup.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE_GROUP)
        }
    }

    override fun getLinkedEntitiesPagingSource(entityId: String, query: String, sorted: Boolean) =
        collectionEntityDao.getReleaseGroupsByCollection(
            collectionId = entityId,
            query = "%$query%",
            sorted = sorted
        )
}
