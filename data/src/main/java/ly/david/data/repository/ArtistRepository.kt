package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.ArtistUiModel
import ly.david.data.domain.toArtistUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.toArtistRoomModel

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
//    private val releasesLabelsDao: ReleasesLabelsDao,
//    private val releaseDao: ReleaseDao,
//    private val relationDao: RelationDao,
) {

    suspend fun lookupArtist(artistId: String): ArtistUiModel {
        val roomArtist = artistDao.getArtist(artistId)
        if (roomArtist != null) {
            return roomArtist.toArtistUiModel()
        }

        val musicBrainzArtist = musicBrainzApiService.lookupArtist(
            artistId = artistId,
        )
        artistDao.insert(musicBrainzArtist.toArtistRoomModel())
        return musicBrainzArtist.toArtistUiModel()
    }

    suspend fun lookupArtistWithRelations(artistId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArtist(
            artistId = artistId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }

//    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
//        val response = musicBrainzApiService.browseReleasesByLabel(
//            labelId = resourceId,
//            offset = nextOffset
//        )
//
//        if (response.offset == 0) {
//            relationDao.insertBrowseResource(
//                browseResourceRoomModel = BrowseResourceOffset(
//                    resourceId = resourceId,
//                    browseResource = MusicBrainzResource.RELEASE,
//                    localCount = response.releases.size,
//                    remoteCount = response.count
//                )
//            )
//        } else {
//            relationDao.incrementOffsetForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
//        }
//
//        val releaseMusicBrainzModels = response.releases
//        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toReleaseRoomModel() })
//        releasesLabelsDao.insertAll(
//            releaseMusicBrainzModels.flatMap { release ->
//                release.labelInfoList?.toReleaseLabels(releaseId = release.id, labelId = resourceId).orEmpty()
//            }
//        )
//
//        return releaseMusicBrainzModels.size
//    }
//
//    // Only difference between this and the stats one is this can return null
//    override suspend fun getRemoteReleasesCountByResource(resourceId: String) =
//        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.remoteCount
//
//    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
//        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0
//
//    override suspend fun deleteReleasesByResource(resourceId: String) {
//        releasesLabelsDao.deleteReleasesByLabel(resourceId)
//        relationDao.deleteBrowseResourceOffsetByResource(resourceId, MusicBrainzResource.RELEASE)
//    }
//
//    override fun getReleasesPagingSource(
//        resourceId: String,
//        query: String
//    ): PagingSource<Int, ReleaseWithReleaseCountries> = when {
//        query.isEmpty() -> {
//            releasesLabelsDao.getReleasesByLabel(resourceId)
//        }
//        else -> {
//            releasesLabelsDao.getReleasesByLabelFiltered(
//                labelId = resourceId,
//                query = "%$query%"
//            )
//        }
//    }
}
