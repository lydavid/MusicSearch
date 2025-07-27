package ly.david.musicsearch.data.repository.series

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository

class SeriesRepositoryImpl(
    private val seriesDao: SeriesDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : SeriesRepository {

    override suspend fun lookupSeries(
        seriesId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): SeriesDetailsModel {
        if (forceRefresh) {
            delete(seriesId)
        }

        val series = seriesDao.getSeriesForDetails(seriesId)
        val urlRelations = relationRepository.getRelationshipsByType(seriesId)
        val visited = relationRepository.visited(seriesId)
        if (series != null &&
            visited &&
            !forceRefresh
        ) {
            return series.copy(
                urls = urlRelations,
            )
        }

        val seriesMusicBrainzModel = lookupApi.lookupSeries(seriesId)
        cache(
            series = seriesMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupSeries(
            seriesId = seriesId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(id: String) {
        seriesDao.withTransaction {
            seriesDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        series: SeriesMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        seriesDao.withTransaction {
            seriesDao.insert(series)

            aliasDao.insertReplaceAll(listOf(series))

            val relationWithOrderList = series.relations.toRelationWithOrderList(series.id)
            relationRepository.insertAllUrlRelations(
                entityId = series.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
