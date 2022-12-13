package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistDao: ArtistDao,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleaseGroups(artistId: String) =
        artistDao.getArtist(artistId)?.releaseGroupsCount ?: 0

    suspend fun getNumberOfReleaseGroupsByArtist(artistId: String) =
        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)

    suspend fun getCountOfEachAlbumType(artistId: String) =
        artistReleaseGroupDao.getCountOfEachAlbumType(artistId)
}
