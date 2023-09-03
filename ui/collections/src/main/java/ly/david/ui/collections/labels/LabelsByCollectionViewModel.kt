package ly.david.ui.collections.labels

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.domain.listitem.toLabelListItemModel
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
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
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val labelDao: LabelDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<LabelRoomModel, LabelListItemModel>,
) : BrowseEntitiesByEntityViewModel<LabelRoomModel, LabelListItemModel, LabelMusicBrainzModel, BrowseLabelsResponse>(
    byEntity = MusicBrainzEntity.LABEL,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseLabelsResponse {
        return musicBrainzApi.browseLabelsByCollection(
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

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, LabelRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getLabelsByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getLabelsByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: LabelRoomModel): LabelListItemModel {
        return roomModel.toLabelListItemModel()
    }
}
