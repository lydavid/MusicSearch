package ly.david.mbjc.data.persistence.release

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class ReleaseDao : BaseDao<ReleaseRoomModel> {

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): ReleaseRoomModel?

    @Query(
        """
        DELETE FROM releases WHERE id = :releaseId
        """
    )
    abstract suspend fun deleteReleaseById(releaseId: String)

    @Query(
        """
        DELETE FROM media WHERE id IN (
        SELECT m.id
        FROM media m
        INNER JOIN releases r ON m.release_id = r.id
        WHERE r.id = :releaseId
        )
        """
    )
    abstract suspend fun deleteMediaAndTracksInRelease(releaseId: String)

    @Query(
        """
            UPDATE releases
            SET cover_art_url = :coverArtUrl
            WHERE id = :releaseId
        """
    )
    abstract suspend fun setReleaseCoverArtUrl(releaseId: String, coverArtUrl: String)
}
