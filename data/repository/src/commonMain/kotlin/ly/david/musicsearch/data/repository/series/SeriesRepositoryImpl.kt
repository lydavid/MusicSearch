package ly.david.musicsearch.data.repository.series

import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.series.SeriesRepository

class SeriesRepositoryImpl(
    private val seriesDao: SeriesDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : SeriesRepository {

    override suspend fun lookupSeries(
        seriesId: String,
        forceRefresh: Boolean,
    ): SeriesDetailsModel {
        if (forceRefresh) {
            delete(seriesId)
        }

        val series = seriesDao.getSeriesForDetails(seriesId)
        val urlRelations = relationRepository.getEntityUrlRelationships(seriesId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(seriesId)
        if (series != null &&
            hasUrlsBeenSavedForEntity &&
            !forceRefresh
        ) {
            return series.copy(
                urls = urlRelations,
            )
        }

        val seriesMusicBrainzModel = lookupApi.lookupSeries(seriesId)
        cache(seriesMusicBrainzModel)
        return lookupSeries(seriesId, false)
    }

    private fun delete(id: String) {
        seriesDao.withTransaction {
            seriesDao.delete(id)
            relationRepository.deleteUrlRelationshipsByEntity(id)
        }
    }

    private fun cache(series: SeriesMusicBrainzModel) {
        seriesDao.withTransaction {
            seriesDao.insert(series)

            val relationWithOrderList = series.relations.toRelationWithOrderList(series.id)
            relationRepository.insertAllUrlRelations(
                entityId = series.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
