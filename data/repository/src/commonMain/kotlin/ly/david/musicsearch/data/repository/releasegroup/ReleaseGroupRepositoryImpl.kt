package ly.david.musicsearch.data.repository.releasegroup

import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository

class ReleaseGroupRepositoryImpl(
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : ReleaseGroupRepository {

    override suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        forceRefresh: Boolean,
    ): ReleaseGroupDetailsModel {
        if (forceRefresh) {
            delete(releaseGroupId)
        }

        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(releaseGroupId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseGroupId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseGroupId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseGroupId)
        if (releaseGroup != null &&
            artistCredits.isNotEmpty() &&
            hasUrlsBeenSavedForEntity &&
            !forceRefresh
        ) {
            return releaseGroup.copy(
                artistCredits = artistCredits,
                urls = urlRelations,
            )
        }

        val releaseGroupMusicBrainzModel = lookupApi.lookupReleaseGroup(releaseGroupId)
        cache(releaseGroupMusicBrainzModel)
        return lookupReleaseGroup(
            releaseGroupId = releaseGroupId,
            forceRefresh = false,
        )
    }

    private fun delete(id: String) {
        releaseGroupDao.withTransaction {
            releaseGroupDao.delete(id)
            relationRepository.deleteUrlRelationshipsByEntity(id)
        }
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
