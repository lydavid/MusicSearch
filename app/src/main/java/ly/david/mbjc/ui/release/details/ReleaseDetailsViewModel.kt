package ly.david.mbjc.ui.release.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.common.UNKNOWN_TIME
import ly.david.data.common.toDisplayTime
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.release.TrackDao

@HiltViewModel
internal class ReleaseDetailsViewModel @Inject constructor(
    private val trackDao: TrackDao,
    private val releasesCountriesDao: ReleasesCountriesDao
) : ViewModel() {
    suspend fun getFormattedReleaseLength(releaseId: String): String {

        val releaseLength = trackDao.getReleaseTracksLength(releaseId).toDisplayTime()
        val hasNulls = trackDao.getReleaseTracksWithNullLength(releaseId) > 0

        return if (hasNulls) {
            if (releaseLength == UNKNOWN_TIME) UNKNOWN_TIME else "$releaseLength (+ $UNKNOWN_TIME)"
        } else {
            releaseLength
        }
    }

    suspend fun getAreasWithReleaseDate(releaseId: String) = releasesCountriesDao.getAreasWithReleaseDate(releaseId)
}
