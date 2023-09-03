package ly.david.ui.collections.areas

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.api.BrowseAreasResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.area.toAreaRoomModel
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class AreasByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<AreaRoomModel, AreaListItemModel>,
) : BrowseEntitiesByEntityViewModel<AreaRoomModel, AreaListItemModel, AreaMusicBrainzModel, BrowseAreasResponse>(
    byEntity = MusicBrainzEntity.AREA,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseAreasResponse {
        return musicBrainzApi.browseAreasByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<AreaMusicBrainzModel>) {
        areaDao.insertAll(musicBrainzModels.map { it.toAreaRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { area ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = area.id
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
    ): PagingSource<Int, AreaRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getAreasByCollection(entityId)
        }

        else -> {
            collectionEntityDao.getAreasByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: AreaRoomModel): AreaListItemModel {
        return roomModel.toAreaListItemModel()
    }
}
