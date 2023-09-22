package ly.david.data.domain.releasegroup

import ly.david.data.core.image.ImageUrlDao
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import org.koin.core.annotation.Single

@Single
class ReleaseGroupRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseGroupDao: ReleaseGroupDao,
    private val imageUrlDao: ImageUrlDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val releaseGroup = releaseGroupDao.getReleaseGroup(releaseGroupId)
        val artistCreditNames = artistCreditDao.getArtistCreditNamesForEntity(releaseGroupId)
        val largeImageUrl = imageUrlDao.getLargeUrlForEntity(releaseGroupId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseGroupId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseGroupId)
        if (releaseGroup != null &&
            artistCreditNames.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return releaseGroup.toReleaseGroupScaffoldModel(
                artistCreditNames = artistCreditNames,
                imageUrl = largeImageUrl,
                urls = urlRelations,
            )
        }

        val releaseGroupMusicBrainzModel = musicBrainzApi.lookupReleaseGroup(releaseGroupId)
        cache(releaseGroupMusicBrainzModel)
        return lookupReleaseGroup(releaseGroupId)
    }

    private fun cache(releaseGroup: ReleaseGroupMusicBrainzModel) {
        releaseGroupDao.withTransaction {
            releaseGroupDao.insert(releaseGroup)
//            artistCreditDao.insertArtistCredits(
//                entityId = releaseGroup.id,
//                artistCredits = releaseGroup.artistCredits,
//            )
            relationRepository.insertAllUrlRelations(
                entityId = releaseGroup.id,
                relationMusicBrainzModels = releaseGroup.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupReleaseGroup(
            releaseGroupId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
