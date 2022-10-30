package ly.david.data.persistence.release

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class TrackDao : BaseDao<TrackRoomModel> {

    companion object {
        private const val TRACKS_IN_RELEASE = """
            FROM tracks t
            INNER JOIN media m ON t.medium_id = m.id
            INNER JOIN releases r ON m.release_id = r.id
            WHERE r.id = :releaseId
        """

        private const val SELECT_TRACKS_IN_RELEASE = """
            SELECT t.*
            $TRACKS_IN_RELEASE
        """

        private const val FILTERED = """
            AND (
                t.title LIKE :query OR t.number LIKE :query
            )
        """
    }

    @Transaction
    @Query(
        """
        $SELECT_TRACKS_IN_RELEASE
    """
    )
    abstract fun getTracksInRelease(releaseId: String): PagingSource<Int, TrackRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_TRACKS_IN_RELEASE
        $FILTERED
    """
    )
    abstract fun getTracksInReleaseFiltered(
        releaseId: String,
        query: String
    ): PagingSource<Int, TrackRoomModel>

    @Transaction
    @Query(
        """
        SELECT SUM(t.length)
        $TRACKS_IN_RELEASE
    """
    )
    abstract suspend fun getReleaseTracksLength(releaseId: String): Int?

    @Transaction
    @Query(
        """
        SELECT COUNT(t.id)
        $TRACKS_IN_RELEASE
        AND t.length IS NULL
    """
    )
    abstract suspend fun getReleaseTracksWithNullLength(releaseId: String): Int
}
