package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.artist.ArtistDao
import ly.david.mbjc.data.persistence.relation.RelationDao

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistDao: ArtistDao,
    private val relationDao: RelationDao
) : ViewModel() {

    suspend fun getTotalReleaseGroups(artistId: String) =
        artistDao.getArtist(artistId)?.releaseGroupsCount ?: 0

    suspend fun getNumberOfReleaseGroupsByArtist(artistId: String) =
        artistDao.getNumberOfReleaseGroupsByArtist(artistId)

    suspend fun getCountOfEachAlbumType(artistId: String) =
        artistDao.getCountOfEachAlbumType(artistId)

    suspend fun getNumberOfRelationsByResource(resourceId: String) =
        relationDao.getNumberOfRelationsByResource(resourceId)

    suspend fun getCountOfEachRelationshipType(resourceId: String) =
        relationDao.getCountOfEachRelationshipType(resourceId)
}
