package ly.david.ui.collections.works

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.data.domain.listitem.toWorkListItemModel
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.api.BrowseWorksResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.work.WorkDao
import ly.david.data.room.work.WorkRoomModel
import ly.david.data.room.work.toWorkRoomModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class WorksByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val workDao: WorkDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<WorkRoomModel, WorkListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<WorkRoomModel, WorkListItemModel, WorkMusicBrainzModel, BrowseWorksResponse>(
    byEntity = MusicBrainzEntity.WORK,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseWorksResponse {
        return musicBrainzApi.browseWorksByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
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
