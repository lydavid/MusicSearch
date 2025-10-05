package ly.david.musicsearch.data.repository.series

import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import kotlin.time.Instant

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
        val cachedData = getCachedData(seriesId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val seriesMusicBrainzModel = lookupApi.lookupSeries(seriesId)
            seriesDao.withTransaction {
                if (forceRefresh) {
                    delete(seriesId)
                }
                cache(
                    oldId = seriesId,
                    series = seriesMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(seriesMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(seriesId: String): SeriesDetailsModel? {
        if (!relationRepository.visited(seriesId)) return null
        val series = seriesDao.getSeriesForDetails(seriesId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(seriesId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.SERIES,
            mbid = seriesId,
        )

        return series.copy(
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        seriesDao.delete(id)
        relationRepository.deleteRelationshipsByType(id)
    }

    private fun cache(
        oldId: String,
        series: SeriesMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        seriesDao.upsert(
            oldId = oldId,
            series = series,
        )

        aliasDao.insertAll(listOf(series))

        val relationWithOrderList = series.relations.toRelationWithOrderList(series.id)
        relationRepository.insertAllUrlRelations(
            entityId = series.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
