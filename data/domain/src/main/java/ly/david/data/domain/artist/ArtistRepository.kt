package ly.david.data.domain.artist

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.artist.ArtistDao
import ly.david.data.room.artist.toArtistRoomModel
import ly.david.data.room.relation.HasUrls
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val relationDao: RelationDao,
) : RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artistWithAllData = artistDao.getArtist(artistId)

        val hasUrls = relationDao.hasUrls(artistId)?.hasUrls == true
        if (artistWithAllData != null && hasUrls) {
            return artistWithAllData.toArtistScaffoldModel()
        }

        val artistMusicBrainzModel = musicBrainzApiService.lookupArtist(
            artistId = artistId,
        )

        artistDao.withTransaction {
            artistDao.insert(artistMusicBrainzModel.toArtistRoomModel())
            val relations = mutableListOf<RelationRoomModel>()
            artistMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    entityId = artistId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)
            relationDao.markEntityHasUrls(
                hasUrls = HasUrls(
                    entityId = artistId,
                    hasUrls = true
                )
            )
        }

        return lookupArtist(artistId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArtist(
            artistId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS
        ).relations
    }
}
