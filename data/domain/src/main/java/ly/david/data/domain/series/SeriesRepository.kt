package ly.david.data.domain.series

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.series.SeriesDao
import ly.david.data.room.series.toSeriesRoomModel

@Singleton
class SeriesRepository @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val seriesDao: SeriesDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupSeries(seriesId: String): SeriesScaffoldModel {
        val seriesWithAllData = seriesDao.getSeries(seriesId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(seriesId)
        if (seriesWithAllData != null && hasUrlsBeenSavedForEntity) {
            return seriesWithAllData.toSeriesScaffoldModel()
        }

        val seriesMusicBrainzModel = musicBrainzApi.lookupSeries(seriesId)
        seriesDao.withTransaction {
            seriesDao.insert(seriesMusicBrainzModel.toSeriesRoomModel())
            relationRepository.insertAllRelations(
                entityId = seriesId,
                relationMusicBrainzModels = seriesMusicBrainzModel.relations,
            )
        }
        return lookupSeries(seriesId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupSeries(
            seriesId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
