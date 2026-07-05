package ly.david.musicsearch.data.repository.base

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.GENERAL_LOOKUP_INCLUDES
import ly.david.musicsearch.data.musicbrainz.api.USER_LOOKUP_INCLUDES
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

abstract class LookupEntityRepository<
    DM : MusicBrainzDetailsModel,
    MB : MusicBrainzNetworkModel,
    >(
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val detailsMetadataDao: DetailsMetadataDao,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
) {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): DM = withContext(coroutineDispatchers.io) {
        val cachedData = getCachedData(entityId)
        return@withContext if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val include = if (musicBrainzAuthStore.userHasAllAuthScopes()) {
                "$GENERAL_LOOKUP_INCLUDES+$USER_LOOKUP_INCLUDES"
            } else {
                GENERAL_LOOKUP_INCLUDES
            }
            val musicBrainzNetworkModel = getRemoteData(
                entityId = entityId,
                include = include,
            )
            withTransaction {
                if (forceRefresh) {
                    delete(entityId)
                }
                cache(
                    oldId = entityId,
                    musicBrainzNetworkModel = musicBrainzNetworkModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(entityId = musicBrainzNetworkModel.id) ?: error("Failed to get cached data")
        }
    }

    abstract fun withTransaction(block: TransactionWithoutReturn.() -> Unit)

    abstract suspend fun getCachedData(entityId: String): DM?

    abstract suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): MB

    open fun delete(entityId: String) {
        relationRepository.deleteRelationshipsByType(
            entityId = entityId,
            entity = MusicBrainzEntityType.URL,
        )
        tagDao.deleteByEntity(entityId = entityId)
    }

    open fun cache(
        oldId: String,
        musicBrainzNetworkModel: MB,
        lastUpdated: Instant,
    ) {
        aliasDao.insertAll(
            musicBrainzNetworkModels = listOf(musicBrainzNetworkModel),
            deleteExisting = true,
        )

        val entityId = musicBrainzNetworkModel.id

        val relationWithOrderList =
            musicBrainzNetworkModel.relations.toRelationWithOrderList(entityId = entityId)
        relationRepository.insertRelations(
            entityId = entityId,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )

        detailsMetadataDao.upsert(
            entityId = entityId,
            lastUpdated = lastUpdated,
        )

        tagDao.insertAll(
            entityId = entityId,
            genres = musicBrainzNetworkModel.genres,
            userGenres = musicBrainzNetworkModel.userGenres,
            tags = musicBrainzNetworkModel.tags,
            userTags = musicBrainzNetworkModel.userTags,
        )
    }

    protected fun visited(entityId: String): Boolean {
        return detailsMetadataDao.contains(entityId)
    }
}
