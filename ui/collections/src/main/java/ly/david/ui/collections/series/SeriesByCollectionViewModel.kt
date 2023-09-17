package ly.david.ui.collections.series

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.data.domain.listitem.toSeriesListItemModel
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.series.SeriesDao
import ly.david.data.room.series.SeriesRoomModel
import ly.david.data.room.series.toSeriesRoomModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.series.SeriesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SeriesByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: RoomCollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val relationDao: RoomRelationDao,
    pagedList: SeriesPagedList,
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
