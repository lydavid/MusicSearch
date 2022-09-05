package ly.david.mbjc.ui.artist.relations

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.toArtistRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel

@Singleton
internal class ArtistRelationsRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val relationDao: RelationDao,
) {
    private var artist: Artist? = null

    suspend fun lookupArtistRelations(artistId: String): Artist =
        artist ?: run {

            // Use cached model.
            val artistRoomModel = artistDao.getArtist(artistId)

            if (artistRoomModel != null && artistRoomModel.hasDefaultRelations) {
                return artistRoomModel
            }

            val artistMusicBrainzModel = musicBrainzApiService.lookupArtist(
                artistId = artistId,
            )

            if (artistRoomModel == null) {
                // TODO: this wouldn't get the total number of release groups
                //  will it mess up release groups screen if it failed lookup?
                artistDao.insert(artistMusicBrainzModel.toArtistRoomModel(hasDefaultRelations = true))
            } else {
                artistDao.setHasDefaultRelations(artistId = artistId, hasDefaultRelations = true)
            }

            val relations = mutableListOf<RelationRoomModel>()
            artistMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = artistId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)

            artistMusicBrainzModel
        }
}
