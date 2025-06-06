package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository

interface TestReleaseGroupRepository {
    val releaseGroupDao: ReleaseGroupDao
    val artistCreditDao: ArtistCreditDao
    val relationsMetadataDao: RelationsMetadataDao
    val visitedDao: DetailsMetadataDao
    val relationDao: RelationDao
    fun createReleaseGroupRepository(
        musicBrainzModel: ReleaseGroupMusicBrainzNetworkModel,
    ): ReleaseGroupRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupReleaseGroup(
                    releaseGroupId: String,
                    include: String,
                ): ReleaseGroupMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = visitedDao,
            relationDao = relationDao,
        )
        return ReleaseGroupRepositoryImpl(
            releaseGroupDao = releaseGroupDao,
            artistCreditDao = artistCreditDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupReleaseGroup(
                    releaseGroupId: String,
                    include: String,
                ): ReleaseGroupMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }
}