package ly.david.musicsearch.data.repository.work

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.api.RECORDING_REL
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository
import kotlin.time.Instant

class WorkRepositoryImpl(
    private val workDao: WorkDao,
    private val workAttributeDao: WorkAttributeDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val lookupApi: LookupApi,
    coroutineDispatchers: CoroutineDispatchers,
    musicBrainzAuthStore: MusicBrainzAuthStore,
    private val appPreferences: AppPreferences,
) : WorkRepository, LookupEntityRepository<WorkDetailsModel, WorkMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    coroutineDispatchers = coroutineDispatchers,
    musicBrainzAuthStore = musicBrainzAuthStore,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        workDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): WorkDetailsModel? {
        if (!relationRepository.visited(entityId)) return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val numberOfListensToShow = appPreferences.observeNumberOfListensInDetails.first()
        val work = workDao.getWorkForDetails(
            workId = entityId,
            listenBrainzUsername = username,
            numberOfListensToShow = numberOfListensToShow,
        ) ?: return null

        val workAttributes = workAttributeDao.getWorkAttributesForWork(entityId)
        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.WORK,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return work.copy(
            attributes = workAttributes.toPersistentList(),
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
            listenCount = work.listenCount.takeIf { username.isNotEmpty() },
        )
    }

    override suspend fun getRemoteData(
        entityId: String,
        include: String,
    ): WorkMusicBrainzNetworkModel {
        return lookupApi.lookupWork(
            workId = entityId,
            include = "$include+$RECORDING_REL",
        )
    }

    override fun delete(entityId: String) {
        super.delete(entityId)

        workDao.delete(entityId)
        relationRepository.deleteRelationshipsByType(entityId = entityId, entity = MusicBrainzEntityType.RECORDING)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: WorkMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        workDao.upsert(
            oldId = oldId,
            work = musicBrainzNetworkModel,
        )
        workAttributeDao.insertAttributesForWork(
            workId = musicBrainzNetworkModel.id,
            musicBrainzNetworkModel.attributes,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
