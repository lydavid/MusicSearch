package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository

class ReleaseGroupRepositoryImpl(
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : ReleaseGroupRepository {

    override suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ReleaseGroupDetailsModel {
        if (forceRefresh) {
            delete(releaseGroupId)
        }

        val cachedData = getCachedData(releaseGroupId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val releaseGroupMusicBrainzModel = lookupApi.lookupReleaseGroup(releaseGroupId)
            cache(
                releaseGroup = releaseGroupMusicBrainzModel,
                lastUpdated = lastUpdated,
            )
            getCachedData(releaseGroupId) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(releaseGroupId: String): ReleaseGroupDetailsModel? {
        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(releaseGroupId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseGroupId)
        val urlRelations = relationRepository.getRelationshipsByType(releaseGroupId)
        val visited = relationRepository.visited(releaseGroupId)

        return if (releaseGroup != null && artistCredits.isNotEmpty() && visited) {
            releaseGroup.copy(
                artistCredits = artistCredits,
                urls = urlRelations,
            )
        } else {
            null
        }
    }

    private fun delete(id: String) {
        releaseGroupDao.withTransaction {
            releaseGroupDao.deleteReleaseGroup(id)
            relationRepository.deleteRelationshipsByType(id)
            artistCreditDao.deleteArtistCreditsForEntity(id)
        }
    }

    private fun cache(
        releaseGroup: ReleaseGroupMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        releaseGroupDao.withTransaction {
            releaseGroupDao.insertReleaseGroup(releaseGroup)

            aliasDao.insertReplaceAll(listOf(releaseGroup))

            val relationWithOrderList = releaseGroup.relations.toRelationWithOrderList(releaseGroup.id)
            relationRepository.insertAllUrlRelations(
                entityId = releaseGroup.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
