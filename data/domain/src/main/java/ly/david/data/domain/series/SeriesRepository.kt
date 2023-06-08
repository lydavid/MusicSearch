package ly.david.data.domain.series

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.data.domain.listitem.toSeriesListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.series.SeriesDao
import ly.david.data.room.series.toSeriesRoomModel

@Singleton
class SeriesRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val seriesDao: SeriesDao,
) : RelationsListRepository {

    suspend fun lookupSeries(seriesId: String): SeriesListItemModel {
        val seriesRoomModel = seriesDao.getSeries(seriesId)
        if (seriesRoomModel != null) {
            return seriesRoomModel.toSeriesListItemModel()
        }

        val seriesMusicBrainzModel = musicBrainzApiService.lookupSeries(seriesId)
        seriesDao.insert(seriesMusicBrainzModel.toSeriesRoomModel())

        return seriesMusicBrainzModel.toSeriesListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupSeries(
            seriesId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
