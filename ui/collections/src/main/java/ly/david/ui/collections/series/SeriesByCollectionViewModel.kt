package ly.david.ui.collections.series

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.data.domain.listitem.toSeriesListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.api.BrowseSeriesResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.series.SeriesDao
import ly.david.data.room.series.SeriesRoomModel
import ly.david.data.room.series.toSeriesRoomModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class SeriesByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<SeriesRoomModel, SeriesListItemModel>,
) : BrowseEntitiesByEntityViewModel<SeriesRoomModel, SeriesListItemModel, SeriesMusicBrainzModel, BrowseSeriesResponse>(
    byEntity = MusicBrainzEntity.SERIES,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseSeriesResponse {
        return musicBrainzApi.browseSeriesByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<SeriesMusicBrainzModel>) {
        seriesDao.insertAll(musicBrainzModels.map { it.toSeriesRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { series ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = series.id
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
    ): PagingSource<Int, SeriesRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getSeriesByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getSeriesByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: SeriesRoomModel): SeriesListItemModel {
        return roomModel.toSeriesListItemModel()
    }
}
