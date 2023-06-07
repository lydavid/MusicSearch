package ly.david.mbjc.ui.collections.labels

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.auth.getBearerToken
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.toLabelListItemModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseLabelsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.label.LabelDao
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.label.toLabelRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class LabelsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val labelDao: LabelDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<LabelRoomModel, LabelListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<LabelRoomModel, LabelListItemModel, LabelMusicBrainzModel, BrowseLabelsResponse>(
    byEntity = MusicBrainzResource.LABEL,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseLabelsResponse {
        return musicBrainzApiService.browseLabelsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<LabelMusicBrainzModel>) {
        labelDao.insertAll(musicBrainzModels.map { it.toLabelRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { label ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = label.id
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
    ): PagingSource<Int, LabelRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getLabelsByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getLabelsByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: LabelRoomModel): LabelListItemModel {
        return roomModel.toLabelListItemModel()
    }
}
