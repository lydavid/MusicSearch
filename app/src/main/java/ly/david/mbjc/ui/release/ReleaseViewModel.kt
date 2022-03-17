package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.network.MusicBrainzRelease
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.ui.Destination

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private var musicBrainzRelease: MusicBrainzRelease? = null

    suspend fun lookupRelease(releaseId: String, ): MusicBrainzRelease =
        musicBrainzRelease ?: musicBrainzApiService.lookupRelease(releaseId).also {
            // TODO: insert release into table or we can do it when getting all release in release group
            //  only artist lookup needed to do it here since we didn't cache the query results
            incrementOrInsertLookupHistory(it)
            musicBrainzRelease = it
        }

    // TODO: see if we can generalize
    private suspend fun incrementOrInsertLookupHistory(musicBrainzRelease: MusicBrainzRelease) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = musicBrainzRelease.getNameWithDisambiguation(),
                destination = Destination.LOOKUP_RELEASE,
                mbid = musicBrainzRelease.id
            )
        )
    }
}
