package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.domain.toArtistListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.toArtistRoomModel

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
): RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistListItemModel {
        val roomArtist = artistDao.getArtist(artistId)
        if (roomArtist != null) {
            return roomArtist.toArtistListItemModel()
        }

        val musicBrainzArtist = musicBrainzApiService.lookupArtist(
            artistId = artistId,
        )
        artistDao.insert(musicBrainzArtist.toArtistRoomModel())
        return musicBrainzArtist.toArtistListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArtist(
            artistId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
