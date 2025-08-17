package ly.david.musicsearch.data.repository.recording

import kotlin.time.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class RecordingRepositoryImpl(
    private val recordingDao: RecordingDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : RecordingRepository {

    override suspend fun lookupRecording(
        recordingId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): RecordingDetailsModel {
        if (forceRefresh) {
            delete(recordingId)
        }

        val cachedData = getCachedData(recordingId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val recordingMusicBrainzModel = lookupApi.lookupRecording(recordingId)
            cache(
                recording = recordingMusicBrainzModel,
                lastUpdated = lastUpdated,
            )
            getCachedData(recordingId) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(recordingId: String): RecordingDetailsModel? {
        val recording = recordingDao.getRecordingForDetails(recordingId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(recordingId)
        val urlRelations = relationRepository.getRelationshipsByType(recordingId)
        val visited = relationRepository.visited(recordingId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RECORDING,
            mbid = recordingId,
        )

        return if (recording != null &&
            artistCredits.isNotEmpty() &&
            visited
        ) {
            recording.copy(
                artistCredits = artistCredits,
                urls = urlRelations,
                aliases = aliases,
            )
        } else {
            null
        }
    }

    private fun delete(id: String) {
        recordingDao.withTransaction {
            recordingDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
            artistCreditDao.deleteArtistCreditsForEntity(id)
        }
    }

    private fun cache(
        recording: RecordingMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        recordingDao.withTransaction {
            recordingDao.insert(recording)

            aliasDao.insertAll(listOf(recording))

            val relationWithOrderList = recording.relations.toRelationWithOrderList(recording.id)
            relationRepository.insertAllUrlRelations(
                entityId = recording.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
