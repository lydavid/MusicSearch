package ly.david.ui.collections.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: RoomCollectionEntityDao,
    private val relationDao: RoomRelationDao,
    releaseDao: ReleaseDao,
    pagedList: ReleasesPagedList,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        collectionEntityDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = release.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            collectionEntityDao.getReleasesByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getReleasesByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }
}
