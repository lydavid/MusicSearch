package ly.david.mbjc.ui.collections.areas

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.toAreaListItemModel
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseAreasResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class AreasByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<AreaRoomModel, AreaListItemModel>,
) : BrowseEntitiesByEntityViewModel
<AreaRoomModel, AreaListItemModel, AreaMusicBrainzModel, BrowseAreasResponse>(
    byEntity = MusicBrainzResource.AREA,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseAreasResponse {
        return musicBrainzApiService.browseAreasByCollection(
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

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.AREA)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, AreaRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getAreasByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getAreasByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: AreaRoomModel): AreaListItemModel {
        return roomModel.toAreaListItemModel()
    }
}
