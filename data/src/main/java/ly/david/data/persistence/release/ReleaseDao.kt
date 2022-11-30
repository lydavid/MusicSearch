package ly.david.data.persistence.release

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.artist.credit.ArtistCreditDao

@Dao
abstract class ReleaseDao : BaseDao<ReleaseRoomModel>, ArtistCreditDao {

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): ReleaseRoomModel?

    @Transaction
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getReleaseWithAllData(releaseId: String): ReleaseWithAllData?

    @Query(
        """
        DELETE FROM releases WHERE id = :releaseId
        """
    )
    abstract suspend fun deleteReleaseById(releaseId: String)

    @Query(
        """
            UPDATE releases
            SET cover_art_url = :coverArtUrl
            WHERE id = :releaseId
        """
    )
    abstract suspend fun setReleaseCoverArtUrl(releaseId: String, coverArtUrl: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllReleasesWithArtistCredits(releases: List<ReleaseMusicBrainzModel>) {
        releases.forEach { release ->
            insertReleaseWithArtistCredits(release)
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReleaseWithArtistCredits(release: ReleaseMusicBrainzModel) {
        insertArtistCredits(artistCredits = release.artistCredits, resourceId = release.id)
        insertReplace(release.toReleaseRoomModel())
    }
}
