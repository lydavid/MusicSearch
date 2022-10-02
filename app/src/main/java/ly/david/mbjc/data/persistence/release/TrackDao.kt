package ly.david.mbjc.data.persistence.release

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class TrackDao : BaseDao<TrackRoomModel> {

    companion object {
        private const val TRACKS_IN_RELEASE = """
            FROM tracks t
            INNER JOIN media m ON t.medium_id = m.id
            INNER JOIN releases r ON m.release_id = r.id
            WHERE r.id = :releaseId
        """

        private const val SELECT_RELEASES_IN_RELEASE_GROUP = """
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
        $SELECT_RELEASES_IN_RELEASE_GROUP
    """
    )
    abstract fun getTracksInRelease(releaseId: String): PagingSource<Int, TrackRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_IN_RELEASE_GROUP
        $FILTERED
    """
    )
    abstract fun getTracksInReleaseFiltered(
        releaseId: String,
        query: String
    ): PagingSource<Int, TrackRoomModel>
}
