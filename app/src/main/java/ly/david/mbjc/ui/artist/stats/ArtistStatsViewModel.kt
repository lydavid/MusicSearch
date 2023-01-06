package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleaseGroups(artistId: String) =
        relationDao.getBrowseResourceCount(artistId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount ?: 0

    suspend fun getNumberOfReleaseGroupsByArtist(artistId: String) =
        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)

    suspend fun getCountOfEachAlbumType(artistId: String) =
        artistReleaseGroupDao.getCountOfEachAlbumType(artistId)
}
