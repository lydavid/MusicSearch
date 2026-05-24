package ly.david.musicsearch.data.repository.base

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import kotlin.time.Instant

abstract class LookupEntityRepository<
    DM : MusicBrainzDetailsModel,
    MB : MusicBrainzNetworkModel,
    >(
    private val coroutineDispatchers: CoroutineDispatchers,
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
            val musicBrainzNetworkModel = getRemoteData(entityId = entityId)
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

    abstract suspend fun getRemoteData(entityId: String): MB

    abstract fun delete(entityId: String)

    abstract fun cache(
        oldId: String,
        musicBrainzNetworkModel: MB,
        lastUpdated: Instant,
    )
}
