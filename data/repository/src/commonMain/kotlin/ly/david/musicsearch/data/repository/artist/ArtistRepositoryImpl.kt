package ly.david.musicsearch.data.repository.artist

import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.LookupArtistApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val areaDao: AreaDao,
    private val lookupArtistApi: LookupArtistApi,
) : ArtistRepository {

    override suspend fun lookupArtistDetails(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel {
        if (forceRefresh) {
            delete(artistId)
        }

        val artistDetailsModel = artistDao.getArtistForDetails(artistId)
        val urlRelations = relationRepository.getEntityUrlRelationships(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)

        if (
            artistDetailsModel != null &&
            hasUrlsBeenSavedForEntity &&
            !forceRefresh
        ) {
            val artistWithUrls = artistDetailsModel.copy(
                urls = urlRelations,
            )
            return artistWithUrls
        }

        val artistMusicBrainzModel = lookupArtistApi.lookupArtist(artistId)
        cache(artistMusicBrainzModel)
        return lookupArtistDetails(
            artistId = artistId,
            forceRefresh = false,
        )
    }

    private fun delete(artistId: String) {
        artistDao.withTransaction {
            artistDao.delete(artistId = artistId)
            relationRepository.deleteUrlRelationshipsByEntity(entityId = artistId)
        }
    }

    private fun cache(artist: ArtistMusicBrainzModel) {
        artistDao.withTransaction {
            artistDao.insertReplace(artist)
            artist.area?.let { area ->
                areaDao.insert(area)
            }

            val relationWithOrderList = artist.relations.toRelationWithOrderList(artist.id)
            relationRepository.insertAllUrlRelations(
                entityId = artist.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
