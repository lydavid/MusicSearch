package ly.david.mbjc.ui.release

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.release.toMediumRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toTrackRoomModel

@Singleton
internal class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
) {
    private var release: ReleaseUiModel? = null

    suspend fun lookupRelease(releaseId: String): Release =
        release ?: run {

            val roomRelease = releaseDao.getRelease(releaseId)
            if (roomRelease?.formats != null && roomRelease.tracks != null) {
                return roomRelease.toReleaseUiModel()
            }

            val musicBrainzRelease = musicBrainzApiService.lookupRelease(releaseId)

            releaseDao.insert(musicBrainzRelease.toReleaseRoomModel())

            // TODO: doing these inserts will slow down loading the title. could we do these async?
            musicBrainzRelease.media?.forEach { medium ->
                val mediumId = mediumDao.insert(medium.toMediumRoomModel(musicBrainzRelease.id))

                trackDao.insertAll(medium.tracks?.map { it.toTrackRoomModel(mediumId) } ?: emptyList())
            }

            musicBrainzRelease
        }
}
