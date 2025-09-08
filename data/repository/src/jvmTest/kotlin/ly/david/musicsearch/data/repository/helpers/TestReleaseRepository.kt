package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleaseRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository

interface TestReleaseRepository {

    val releaseDao: ReleaseDao
    val releaseReleaseGroupDao: ReleaseReleaseGroupDao
    val releaseGroupDao: ReleaseGroupDao
    val artistCreditDao: ArtistCreditDao
    val areaDao: AreaDao
    val labelDao: LabelDao
    val mediumDao: MediumDao
    val trackDao: TrackDao
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val aliasDao: AliasDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createReleaseRepository(
        musicBrainzModel: ReleaseMusicBrainzNetworkModel,
        fakeBrowseUsername: String = "",
    ): ReleaseRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRelease(
                    releaseId: String,
                    include: String,
                ): ReleaseMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return ReleaseRepositoryImpl(
            releaseDao = releaseDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
            releaseGroupDao = releaseGroupDao,
            artistCreditDao = artistCreditDao,
            areaDao = areaDao,
            labelDao = labelDao,
            relationRepository = relationRepository,
            mediumDao = mediumDao,
            trackDao = trackDao,
            aliasDao = aliasDao,
            listenBrainzRepository = object : ListenBrainzRepository {
                override fun getBaseUrl(): String = ""
            },
            listenBrainzAuthStore = object : NoOpListenBrainzAuthStore() {
                override val browseUsername: Flow<String>
                    get() = flowOf(fakeBrowseUsername)
            },
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRelease(
                    releaseId: String,
                    include: String,
                ): ReleaseMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}
