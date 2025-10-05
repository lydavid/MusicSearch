package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import kotlin.time.Instant

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
        val cachedData = getCachedData(releaseGroupId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val releaseGroupMusicBrainzModel = lookupApi.lookupReleaseGroup(releaseGroupId)
            releaseGroupDao.withTransaction {
                if (forceRefresh) {
                    delete(releaseGroupId)
                }
                cache(
                    oldId = releaseGroupId,
                    releaseGroup = releaseGroupMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(releaseGroupId) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(releaseGroupId: String): ReleaseGroupDetailsModel? {
        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(releaseGroupId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseGroupId)
        val urlRelations = relationRepository.getRelationshipsByType(releaseGroupId)
        val visited = relationRepository.visited(releaseGroupId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.RELEASE_GROUP,
            mbid = releaseGroupId,
        )

        return if (releaseGroup != null && artistCredits.isNotEmpty() && visited) {
            releaseGroup.copy(
                artistCredits = artistCredits.toPersistentList(),
                urls = urlRelations,
                aliases = aliases,
            )
        } else {
            null
        }
    }

    private fun delete(id: String) {
        releaseGroupDao.deleteReleaseGroup(id)
        relationRepository.deleteRelationshipsByType(id)
        artistCreditDao.deleteArtistCreditsForEntity(id)
    }

    private fun cache(
        oldId: String,
        releaseGroup: ReleaseGroupMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        releaseGroupDao.upsertReleaseGroup(
            oldId = oldId,
            releaseGroup = releaseGroup,
        )

        aliasDao.insertAll(listOf(releaseGroup))

        val relationWithOrderList = releaseGroup.relations.toRelationWithOrderList(releaseGroup.id)
        relationRepository.insertAllUrlRelations(
            entityId = releaseGroup.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
