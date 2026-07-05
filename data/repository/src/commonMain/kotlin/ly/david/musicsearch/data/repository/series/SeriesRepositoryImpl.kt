package ly.david.musicsearch.data.repository.series

import app.cash.sqldelight.TransactionWithoutReturn
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import kotlin.time.Instant

class SeriesRepositoryImpl(
    private val seriesDao: SeriesDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    detailsMetadataDao: DetailsMetadataDao,
    coroutineDispatchers: CoroutineDispatchers,
    musicBrainzAuthStore: MusicBrainzAuthStore,
) : SeriesRepository, LookupEntityRepository<SeriesDetailsModel, SeriesMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    detailsMetadataDao = detailsMetadataDao,
    coroutineDispatchers = coroutineDispatchers,
    musicBrainzAuthStore = musicBrainzAuthStore,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        seriesDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): SeriesDetailsModel? {
        if (!relationRepository.visited(entityId)) return null
        val series = seriesDao.getSeriesForDetails(entityId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.SERIES,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return series.copy(
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): SeriesMusicBrainzNetworkModel {
        return lookupApi.lookupSeries(
            seriesId = entityId,
            include = include,
        )
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        seriesDao.delete(entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: SeriesMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        seriesDao.upsert(
            oldId = oldId,
            series = musicBrainzNetworkModel,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
