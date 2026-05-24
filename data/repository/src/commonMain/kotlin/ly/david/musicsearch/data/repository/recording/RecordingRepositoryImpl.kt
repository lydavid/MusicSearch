package ly.david.musicsearch.data.repository.recording

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
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
    private val tagDao: TagDao,
    coroutineDispatchers: CoroutineDispatchers,
    private val appPreferences: AppPreferences,
) : RecordingRepository, LookupEntityRepository<RecordingDetailsModel, RecordingMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    coroutineDispatchers = coroutineDispatchers,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        recordingDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): RecordingDetailsModel? {
        if (!relationRepository.visited(entityId)) return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val numberOfListensToShow = appPreferences.observeNumberOfListensInDetails.first()
        val recording = recordingDao.getRecordingForDetails(
            recordingId = entityId,
            listenBrainzUsername = username,
            numberOfListensToShow = numberOfListensToShow,
        ) ?: return null

        val artistCredits = artistCreditDao.getArtistCreditsForEntity(entityId)
        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RECORDING,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return recording.copy(
            artistCredits = artistCredits.toPersistentList(),
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
            listenCount = recording.listenCount.takeIf { username.isNotEmpty() },
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/track/$entityId",
        )
    }

    override suspend fun getRemoteData(entityId: String): RecordingMusicBrainzNetworkModel {
        return lookupApi.lookupRecording(recordingId = entityId)
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        recordingDao.delete(entityId)
        artistCreditDao.deleteArtistCreditsForEntity(entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: RecordingMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        recordingDao.upsert(
            oldId = oldId,
            recording = musicBrainzNetworkModel,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
