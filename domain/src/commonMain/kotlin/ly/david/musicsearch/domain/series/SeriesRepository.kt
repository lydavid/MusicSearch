package ly.david.musicsearch.domain.series

import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class SeriesRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val seriesDao: SeriesDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupSeries(seriesId: String): SeriesScaffoldModel {
        val series = seriesDao.getSeries(seriesId)
        val urlRelations = relationRepository.getEntityUrlRelationships(seriesId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(seriesId)
        if (series != null && hasUrlsBeenSavedForEntity) {
            return series.toSeriesScaffoldModel(
                urls = urlRelations,
            )
        }

        val seriesMusicBrainzModel = musicBrainzApi.lookupSeries(seriesId)
        cache(seriesMusicBrainzModel)
        return lookupSeries(seriesId)
    }

    private fun cache(series: SeriesMusicBrainzModel) {
        seriesDao.withTransaction {
            seriesDao.insert(series)
            relationRepository.insertAllUrlRelations(
                entityId = series.id,
                relationMusicBrainzModels = series.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupSeries(
            seriesId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
