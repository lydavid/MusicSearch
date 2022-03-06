package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Release
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

    private var release: Release? = null

    suspend fun lookupRelease(releaseId: String, ): Release =
        release ?: musicBrainzApiService.lookupRelease(releaseId).also {
            // TODO: insert release into table or we can do it when getting all release in release group
            //  only artist lookup needed to do it here since we didn't cache the query results
            incrementOrInsertLookupHistory(it)
            release = it
        }

    // TODO: see if we can generalize
    private suspend fun incrementOrInsertLookupHistory(release: Release) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = release.getNameWithDisambiguation(),
                destination = Destination.LOOKUP_RELEASE,
                mbid = release.id
            )
        )
    }
}
