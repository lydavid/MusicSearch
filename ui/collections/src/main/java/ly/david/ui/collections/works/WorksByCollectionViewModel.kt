package ly.david.ui.collections.works

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.data.domain.listitem.toWorkListItemModel
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseWorksResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.work.RoomWorkDao
import ly.david.data.room.work.WorkRoomModel
import ly.david.data.room.work.toWorkRoomModel
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.work.WorksPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorksByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: RoomCollectionEntityDao,
    private val workDao: RoomWorkDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: WorksPagedList,
) : BrowseEntitiesByEntityViewModel<WorkRoomModel, WorkListItemModel, WorkMusicBrainzModel, BrowseWorksResponse>(
    byEntity = MusicBrainzEntity.WORK,
    browseEntityCountDao = browseEntityCountDao,
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
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
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
