package ly.david.musicsearch.data.repository.artist

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val areaDao: AreaDao,
    private val aliasDao: AliasDao,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val lookupApi: LookupApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ArtistRepository {

    override suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ArtistDetailsModel = withContext(coroutineDispatchers.io) {
        val cachedData = getCachedData(artistId)
        return@withContext if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val artistMusicBrainzModel = lookupApi.lookupArtist(artistId)
            artistDao.withTransaction {
                if (forceRefresh) {
                    delete(artistId)
                }
                cache(
                    oldId = artistId,
                    artist = artistMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(artistMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private suspend fun getCachedData(artistId: String): ArtistDetailsModel? {
        if (!relationRepository.visited(artistId)) return null

        val username = listenBrainzAuthStore.browseUsername.first()
        val artist = artistDao.getArtistForDetails(
            artistId = artistId,
            listenBrainzUsername = username,
        ) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(artistId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.ARTIST,
            mbid = artistId,
        )

        return artist.copy(
            urls = urlRelations,
            aliases = aliases,
            listenBrainzUrl = "${listenBrainzRepository.getBaseUrl()}/artist/${artist.id}",
        )
    }

    private fun delete(artistId: String) {
        artistDao.delete(artistId = artistId)
        relationRepository.deleteRelationshipsByType(
            entityId = artistId,
            entity = MusicBrainzEntityType.URL,
        )
    }

    private fun cache(
        oldId: String,
        artist: ArtistMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        artistDao.upsert(
            oldId = oldId,
            artist = artist,
        )

        aliasDao.insertAll(listOf(artist))

        artist.area?.let { area ->
            areaDao.insert(area)
        }

        val relationWithOrderList = artist.relations.toRelationWithOrderList(artist.id)
        relationRepository.insertRelations(
            entityId = artist.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
