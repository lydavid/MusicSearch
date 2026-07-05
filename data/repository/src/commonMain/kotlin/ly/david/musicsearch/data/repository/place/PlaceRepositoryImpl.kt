package ly.david.musicsearch.data.repository.place

import app.cash.sqldelight.TransactionWithoutReturn
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    detailsMetadataDao: DetailsMetadataDao,
    coroutineDispatchers: CoroutineDispatchers,
    musicBrainzAuthStore: MusicBrainzAuthStore,
) : PlaceRepository, LookupEntityRepository<PlaceDetailsModel, PlaceMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    detailsMetadataDao = detailsMetadataDao,
    coroutineDispatchers = coroutineDispatchers,
    musicBrainzAuthStore = musicBrainzAuthStore,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        placeDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): PlaceDetailsModel? {
        if (!relationRepository.visited(entityId)) return null
        val place = placeDao.getPlaceForDetails(entityId) ?: return null

        val area = areaDao.getAreaByPlace(entityId)
        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.PLACE,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return place.copy(
            area = area,
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): PlaceMusicBrainzNetworkModel {
        return lookupApi.lookupPlace(
            placeId = entityId,
            include = include,
        )
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        placeDao.delete(entityId)
        areaDao.deleteAreaPlaceLink(entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: PlaceMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        placeDao.upsert(
            oldId = oldId,
            place = musicBrainzNetworkModel,
        )

        musicBrainzNetworkModel.area?.let { areaMusicBrainzModel ->
            areaDao.insert(areaMusicBrainzModel)
            placeDao.insertPlaceByArea(
                entityId = areaMusicBrainzModel.id,
                placeId = musicBrainzNetworkModel.id,
            )
        }

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
