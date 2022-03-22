package ly.david.mbjc.data.persistence.release

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.RoomRelease

@Dao
abstract class ReleaseDao : BaseDao<RoomRelease> {

    companion object {
        private const val RELEASES_IN_RELEASE_GROUP = """
            SELECT r.*
            FROM releases r
            INNER JOIN releases_release_groups rrg ON r.id = rrg.release_id
            INNER JOIN release_groups rg ON rg.id = rrg.release_group_id
            WHERE rg.id = :releaseGroupId
        """

        // TODO:
        private const val FILTERED = """
            AND (r.name LIKE :query OR r.disambiguation LIKE :query)
        """
    }

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): RoomRelease?

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM releases r
            INNER JOIN releases_release_groups rrg ON r.id = rrg.release_id
            INNER JOIN release_groups rg ON rg.id = rrg.release_group_id
            WHERE rg.id = :releaseGroupId
            GROUP BY rg.id),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesInReleaseGroup(releaseGroupId: String): Int

    @Transaction
    @Query(RELEASES_IN_RELEASE_GROUP)
    abstract fun getReleasesInReleaseGroup(releaseGroupId: String): PagingSource<Int, RoomRelease>

    @Transaction
    @Query(
        """
        $RELEASES_IN_RELEASE_GROUP
        $FILTERED
    """
    )
    abstract fun getReleasesInReleaseGroupFiltered(
        releaseGroupId: String,
        query: String
    ): PagingSource<Int, RoomRelease>




}
