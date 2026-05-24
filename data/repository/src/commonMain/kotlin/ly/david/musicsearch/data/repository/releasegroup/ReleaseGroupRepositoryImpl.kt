package ly.david.musicsearch.data.repository.releasegroup

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import kotlin.time.Instant

class ReleaseGroupRepositoryImpl(
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    coroutineDispatchers: CoroutineDispatchers,
) : ReleaseGroupRepository, LookupEntityRepository<ReleaseGroupDetailsModel, ReleaseGroupMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    coroutineDispatchers = coroutineDispatchers,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        releaseGroupDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): ReleaseGroupDetailsModel? {
        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(entityId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(entityId)
        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val visited = relationRepository.visited(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RELEASE_GROUP,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return if (releaseGroup != null && artistCredits.isNotEmpty() && visited) {
            releaseGroup.copy(
                artistCredits = artistCredits.toPersistentList(),
                urls = urlRelations,
                aliases = aliases,
                genres = genres,
                tags = tags,
            )
        } else {
            null
        }
    }

    override suspend fun getRemoteData(entityId: String): ReleaseGroupMusicBrainzNetworkModel {
        return lookupApi.lookupReleaseGroup(releaseGroupId = entityId)
    }

    override fun delete(entityId: String) {
        releaseGroupDao.deleteReleaseGroup(entityId)
        relationRepository.deleteRelationshipsByType(entityId)
        artistCreditDao.deleteArtistCreditsForEntity(entityId)
        tagDao.deleteByEntity(entityId = entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: ReleaseGroupMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        releaseGroupDao.upsertReleaseGroup(
            oldId = oldId,
            releaseGroup = musicBrainzNetworkModel,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
