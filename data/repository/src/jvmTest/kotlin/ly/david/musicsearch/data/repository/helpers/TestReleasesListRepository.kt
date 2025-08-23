package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.release.ReleasesListRepositoryImpl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository

interface TestReleasesListRepository {
    val releaseDao: ReleaseDao
    val collectionEntityDao: CollectionEntityDao
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao
    val aliasDao: AliasDao

    fun createReleasesListRepository(
        releases: List<ReleaseMusicBrainzNetworkModel>,
    ): ReleasesListRepository {
        return ReleasesListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            releaseDao = releaseDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseReleasesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
                ) = BrowseReleasesResponse(
                    count = 1,
                    offset = 0,
                    musicBrainzModels = releases,
                )
            },
        )
    }
}
