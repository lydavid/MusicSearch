package ly.david.musicsearch.data.repository.artist

import app.cash.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.flow.first
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val areaDao: AreaDao,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val lookupApi: LookupApi,
    coroutineDispatchers: CoroutineDispatchers,
    private val appPreferences: AppPreferences,
) : ArtistRepository, LookupEntityRepository<ArtistDetailsModel, ArtistMusicBrainzNetworkModel>(
    coroutineDispatchers = coroutineDispatchers,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        artistDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): ArtistDetailsModel? {
        if (!relationRepository.visited(entityId)) return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val numberOfListensToShow = appPreferences.observeNumberOfListensInDetails.first()
        val artist = artistDao.getArtistForDetails(
            artistId = entityId,
            listenBrainzUsername = username,
            numberOfListensToShow = numberOfListensToShow,
        ) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.ARTIST,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return artist.copy(
            urls = urlRelations,
            aliases = aliases,
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/artist/${artist.id}",
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(entityId: String): ArtistMusicBrainzNetworkModel {
        return lookupApi.lookupArtist(artistId = entityId)
    }

    override fun delete(entityId: String) {
        artistDao.delete(artistId = entityId)
        relationRepository.deleteRelationshipsByType(
            entityId = entityId,
            entity = MusicBrainzEntityType.URL,
        )
        tagDao.deleteByEntity(entityId = entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: ArtistMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        artistDao.upsert(
            oldId = oldId,
            artist = musicBrainzNetworkModel,
        )

        aliasDao.insertAll(listOf(musicBrainzNetworkModel))

        musicBrainzNetworkModel.area?.let { area ->
            areaDao.insert(area)
        }

        val relationWithOrderList =
            musicBrainzNetworkModel.relations.toRelationWithOrderList(entityId = musicBrainzNetworkModel.id)
        relationRepository.insertRelations(
            entityId = musicBrainzNetworkModel.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )

        tagDao.insertAll(
            entityId = musicBrainzNetworkModel.id,
            genres = musicBrainzNetworkModel.genres,
            tags = musicBrainzNetworkModel.tags,
        )
    }
}
