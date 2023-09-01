package ly.david.ui.collections.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.paging.PagedList
import ly.david.ui.common.release.ReleasesByEntityViewModel

@HiltViewModel
internal class ReleasesByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
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
