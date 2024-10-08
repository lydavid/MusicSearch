package ly.david.musicsearch.data.repository.releasegroup

import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository

class ReleaseGroupRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
) : ReleaseGroupRepository {

    override suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupDetailsModel {
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
