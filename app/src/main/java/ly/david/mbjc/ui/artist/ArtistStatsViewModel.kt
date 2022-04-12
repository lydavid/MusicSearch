package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao

@HiltViewModel
class ArtistStatsViewModel @Inject constructor(
    private val artistDao: ArtistDao,
    private val releaseGroupDao: ReleaseGroupDao
) : ViewModel() {

    suspend fun getTotalReleaseGroups(artistId: String) = artistDao.getArtist(artistId)?.releaseGroupsCount ?: 0

    suspend fun getNumberOfReleaseGroupsByArtist(artistId: String) =
        releaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)

    suspend fun getCountOfEachAlbumType(artistId: String) =
        releaseGroupDao.getCountOfEachAlbumType(artistId)
}
