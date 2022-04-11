package ly.david.mbjc.ui.release

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.release.toRoomMedium
import ly.david.mbjc.data.persistence.release.toRoomTrack
import ly.david.mbjc.data.persistence.toRoomRelease
import ly.david.mbjc.ui.Destination

@Singleton
class ReleaseRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var release: ReleaseUiModel? = null

    // We need UiReleaseGroup so that we have artist credits
    suspend fun lookupRelease(releaseId: String): Release =
        release ?: run {

            val roomRelease = releaseDao.getRelease(releaseId)
            if (roomRelease?.formats != null && roomRelease.tracks != null) {
                incrementOrInsertLookupHistory(roomRelease)

                return roomRelease.toReleaseUiModel()
            }

            val musicBrainzRelease = musicBrainzApiService.lookupRelease(releaseId)

            releaseDao.insert(musicBrainzRelease.toRoomRelease())

            // TODO: doing these inserts will slow down loading the title. could we do these async?
            musicBrainzRelease.media?.forEach { medium ->
                val mediumId = mediumDao.insert(medium.toRoomMedium(musicBrainzRelease.id))

                trackDao.insertAll(medium.tracks?.map { it.toRoomTrack(mediumId) } ?: emptyList())
            }

            incrementOrInsertLookupHistory(musicBrainzRelease)

            musicBrainzRelease
        }

    private suspend fun incrementOrInsertLookupHistory(release: Release) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = release.name,
                destination = Destination.LOOKUP_RELEASE,
                mbid = release.id
            )
        )
    }
}
