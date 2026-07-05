package ly.david.musicsearch.data.repository.area

import app.cash.sqldelight.TransactionWithoutReturn
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class AreaRepositoryImpl(
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    detailsMetadataDao: DetailsMetadataDao,
    coroutineDispatchers: CoroutineDispatchers,
    musicBrainzAuthStore: MusicBrainzAuthStore,
) : AreaRepository, LookupEntityRepository<AreaDetailsModel, AreaMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    detailsMetadataDao = detailsMetadataDao,
    coroutineDispatchers = coroutineDispatchers,
    musicBrainzAuthStore = musicBrainzAuthStore,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        areaDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): AreaDetailsModel? {
        if (!relationRepository.visited(entityId)) return null

        val area = areaDao.getAreaForDetails(entityId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.AREA,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return area.copy(
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): AreaMusicBrainzNetworkModel {
        return lookupApi.lookupArea(
            areaId = entityId,
            include = include,
        )
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        areaDao.delete(entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: AreaMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        areaDao.upsert(
            oldAreaId = oldId,
            area = musicBrainzNetworkModel,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
