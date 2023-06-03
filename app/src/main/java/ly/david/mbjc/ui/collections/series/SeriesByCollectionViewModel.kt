package ly.david.mbjc.ui.collections.series

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.auth.getBearerToken
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.domain.toSeriesListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.api.BrowseSeriesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.series.SeriesDao
import ly.david.data.persistence.series.SeriesRoomModel
import ly.david.data.persistence.series.toSeriesRoomModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class SeriesByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<SeriesRoomModel, SeriesListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<SeriesRoomModel, SeriesListItemModel, SeriesMusicBrainzModel, BrowseSeriesResponse>(
    byEntity = MusicBrainzResource.SERIES,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseSeriesResponse {
        return musicBrainzApiService.browseSeriesByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
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

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.AREA)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, SeriesRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getSeriesByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getSeriesByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: SeriesRoomModel): SeriesListItemModel {
        return roomModel.toSeriesListItemModel()
    }
}
