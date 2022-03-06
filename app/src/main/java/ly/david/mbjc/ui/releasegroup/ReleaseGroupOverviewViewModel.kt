package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.LookupHistory
import ly.david.mbjc.data.LookupHistoryDao
import ly.david.mbjc.data.MusicBrainzApiService
import ly.david.mbjc.data.MusicBrainzReleaseGroup
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.ReleaseGroupDao
import ly.david.mbjc.data.toRoomReleaseGroup
import ly.david.mbjc.ui.Destination

@HiltViewModel
class ReleaseGroupOverviewViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var releaseGroup: MusicBrainzReleaseGroup? = null

    suspend fun lookupReleaseGroup(releaseGroupId: String): MusicBrainzReleaseGroup =
        releaseGroup ?: musicBrainzApiService.lookupReleaseGroup(releaseGroupId).also {

            releaseGroupDao.insert(it.toRoomReleaseGroup())
            incrementOrInsertLookupHistory(it)
            releaseGroup = it
        }

    // TODO: see if we can generalize
    private suspend fun incrementOrInsertLookupHistory(releaseGroup: ReleaseGroup) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = releaseGroup.title,
                destination = Destination.LOOKUP_RELEASE_GROUP,
                mbid = releaseGroup.id
            )
        )
    }
}
