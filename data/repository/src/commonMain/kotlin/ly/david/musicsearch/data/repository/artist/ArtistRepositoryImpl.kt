package ly.david.musicsearch.data.repository.artist

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.artist.MembersAndGroups
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.relatableEntities
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val areaDao: AreaDao,
    private val lookupApi: LookupApi,
) : ArtistRepository {

    override suspend fun lookupArtistDetails(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel {
        if (forceRefresh) {
            delete(artistId)
        }

        val artistDetailsModel = artistDao.getArtistForDetails(artistId)
        val visited = relationRepository.visited(artistId)

        if (
            artistDetailsModel != null &&
            visited &&
            !forceRefresh
        ) {
            return artistDetailsModel
        }

        val artistMusicBrainzModel = lookupApi.lookupArtist(artistId)
        cache(artistMusicBrainzModel)
        return lookupArtistDetails(
            artistId = artistId,
            forceRefresh = false,
        )
    }

    private fun delete(artistId: String) {
        artistDao.withTransaction {
            artistDao.delete(artistId = artistId)
            relationRepository.deleteRelationshipsByType(
                entityId = artistId,
                entity = MusicBrainzEntity.URL,
            )
            relationRepository.deleteRelationshipsByType(
                entityId = artistId,
                entity = MusicBrainzEntity.ARTIST,
            )
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

    private fun List<RelationListItemModel>.filterAndSplit(): MembersAndGroups {
        val artists: List<RelationListItemModel> = filter {
            it.isForwardDirection != null && it.label == "member of band"
        }

        return MembersAndGroups(
            partOfGroups = artists.filter { it.isForwardDirection == true && it.lifeSpan.ended == false },
            previouslyPartOfGroups = artists.filter { it.isForwardDirection == true && it.lifeSpan.ended == true },
            membersOfGroup = artists.filter { it.isForwardDirection == false && it.lifeSpan.ended == false },
            previousMembersOfGroup = artists.filter { it.isForwardDirection == false && it.lifeSpan.ended == true },
        )
    }
}
