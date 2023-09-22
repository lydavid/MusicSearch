package ly.david.data.room.release

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.room.BaseDao
import ly.david.data.room.artist.credit.ArtistCreditDao

@Dao
abstract class RoomReleaseDao : BaseDao<ReleaseRoomModel>(), ArtistCreditDao {

    @Transaction
    open suspend fun insertAllReleasesWithArtistCredits(releases: List<ReleaseMusicBrainzModel>) {
        releases.forEach { release ->
            insertReleaseWithArtistCredits(release)
        }
    }

    @Transaction
    open suspend fun insertReleaseWithArtistCredits(release: ReleaseMusicBrainzModel) {
        insertArtistCredits(artistCredits = release.artistCredits, entityId = release.id)
        insertReplace(release.toRoomModel())
    }

    // Lookup
    @Transaction
    @Query("SELECT * FROM `release` WHERE id = :releaseId")
    abstract suspend fun getReleaseWithFormatTrackCounts(releaseId: String): ReleaseWithFormatTrackCounts?

    // TODO: simplify
    @Transaction
    @Query(
        """
        SELECT *, 
        (
            SELECT SUM(t.length)
            FROM track t
            INNER JOIN medium m ON t.medium_id = m.id
            INNER JOIN `release` r ON m.release_id = r.id
            WHERE r.id = :releaseId
        ) as releaseLength,
        (
            SELECT COUNT(t.id) > 0
            FROM track t
            INNER JOIN medium m ON t.medium_id = m.id
            INNER JOIN `release` r ON m.release_id = r.id
            WHERE r.id = :releaseId
            AND t.length IS NULL
        ) as hasNullLength
        FROM `release`
        WHERE id = :releaseId
        """
    )
    abstract suspend fun getReleaseWithAllData(releaseId: String): ReleaseWithAllData?

    /**
     * By deleting a release, all junction tables for it should cascade delete too.
     */
    @Query(
        """
        DELETE FROM `release` WHERE id = :releaseId
        """
    )
    abstract suspend fun deleteReleaseById(releaseId: String)
}
