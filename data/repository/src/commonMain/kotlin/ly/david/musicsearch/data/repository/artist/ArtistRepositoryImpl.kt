package ly.david.musicsearch.data.repository.artist

import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val areaDao: AreaDao,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ArtistRepository {

    override suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ArtistDetailsModel = withContext(coroutineDispatchers.io) {
        if (forceRefresh) {
            delete(artistId)
        }

        val artistDetailsModel = artistDao.getArtistForDetails(artistId)
        val urlRelations = relationRepository.getRelationshipsByType(artistId)
        val visited = relationRepository.visited(artistId)

        if (
            artistDetailsModel != null &&
            visited &&
            !forceRefresh
        ) {
            val artistWithUrls = artistDetailsModel.copy(
                urls = urlRelations,
            )
            return@withContext artistWithUrls
        }

        val artistMusicBrainzModel = lookupApi.lookupArtist(artistId)
        cache(
            artist = artistMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return@withContext lookupArtist(
            artistId = artistId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(artistId: String) {
        artistDao.withTransaction {
            artistDao.delete(artistId = artistId)
            relationRepository.deleteRelationshipsByType(
                entityId = artistId,
                entity = MusicBrainzEntity.URL,
            )
        }
    }

    private fun cache(
        artist: ArtistMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        artistDao.withTransaction {
            artistDao.insertReplace(artist)

            aliasDao.insertReplaceAll(listOf(artist))

            artist.area?.let { area ->
                areaDao.insert(area)
            }

            val relationWithOrderList = artist.relations.toRelationWithOrderList(artist.id)
            relationRepository.insertAllUrlRelations(
                entityId = artist.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
