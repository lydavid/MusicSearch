package ly.david.musicsearch.data.repository

import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository

class ReleaseGroupRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
) : ReleaseGroupRepository {

    override suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(releaseGroupId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseGroupId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseGroupId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseGroupId)
        if (releaseGroup != null &&
            artistCredits.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return releaseGroup.copy(
                artistCredits = artistCredits,
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

            val relationWithOrderList = releaseGroup.relations.toRelationWithOrderList(releaseGroup.id)
            relationRepository.insertAllUrlRelations(
                entityId = releaseGroup.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
