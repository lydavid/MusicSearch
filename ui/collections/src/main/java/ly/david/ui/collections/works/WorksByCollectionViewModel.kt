package ly.david.ui.collections.works

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.data.domain.listitem.toWorkListItemModel
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseWorksResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.work.WorkDao
import ly.david.data.room.work.WorkRoomModel
import ly.david.data.room.work.toWorkRoomModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.work.WorksPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorksByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val workDao: WorkDao,
    private val relationDao: RoomRelationDao,
    pagedList: WorksPagedList,
) : BrowseEntitiesByEntityViewModel<WorkRoomModel, WorkListItemModel, WorkMusicBrainzModel, BrowseWorksResponse>(
    byEntity = MusicBrainzEntity.WORK,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseWorksResponse {
        return musicBrainzApi.browseWorksByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<WorkMusicBrainzModel>) {
        workDao.insertAll(musicBrainzModels.map { it.toWorkRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { work ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = work.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, WorkRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getWorksByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getWorksByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: WorkRoomModel): WorkListItemModel {
        return roomModel.toWorkListItemModel()
    }
}
