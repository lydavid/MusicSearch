// https://github.com/detekt/detekt/issues/8140
@file:Suppress("SpacingAroundColon", "NoUnusedImports", "Wrapping")

package ly.david.musicsearch.data.repository.recording

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class RecordingRepositoryImpl(
    private val recordingDao: RecordingDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val lookupApi: LookupApi,
) : RecordingRepository {

    override suspend fun lookupRecording(
        recordingId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): RecordingDetailsModel {
        val cachedData = getCachedData(recordingId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val recordingMusicBrainzModel = lookupApi.lookupRecording(recordingId)
            recordingDao.withTransaction {
                if (forceRefresh) {
                    delete(recordingId)
                }
                cache(
                    oldId = recordingId,
                    recording = recordingMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(recordingMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private suspend fun getCachedData(recordingId: String): RecordingDetailsModel? {
        if (!relationRepository.visited(recordingId)) return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val recording = recordingDao.getRecordingForDetails(
            recordingId = recordingId,
            listenBrainzUsername = username,
        ) ?: return null

        val artistCredits = artistCreditDao.getArtistCreditsForEntity(recordingId)
        val urlRelations = relationRepository.getRelationshipsByType(recordingId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RECORDING,
            mbid = recordingId,
        )

        return recording.copy(
            artistCredits = artistCredits.toPersistentList(),
            urls = urlRelations,
            aliases = aliases,
            listenCount = recording.listenCount.takeIf { username.isNotEmpty() },
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/track/$recordingId",
        )
    }

    private fun delete(id: String) {
        recordingDao.delete(id)
        relationRepository.deleteRelationshipsByType(id)
        artistCreditDao.deleteArtistCreditsForEntity(id)
    }

    context(_: TransactionWithoutReturn)
    private fun cache(
        oldId: String,
        recording: RecordingMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        recordingDao.upsert(
            oldId = oldId,
            recording = recording,
        )

        aliasDao.insertAll(listOf(recording))

        val relationWithOrderList = recording.relations.toRelationWithOrderList(recording.id)
        relationRepository.insertAllUrlRelations(
            entityId = recording.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
