package ly.david.mbjc.data.persistence.releasegroup

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel

@Dao
internal abstract class ReleasesReleaseGroupsDao : BaseDao<ReleasesReleaseGroups> {

    companion object {
        private const val RELEASES_IN_RELEASE_GROUP = """
            FROM releases r
            INNER JOIN releases_release_groups rrg ON r.id = rrg.release_id
            INNER JOIN release_groups rg ON rg.id = rrg.release_group_id
            WHERE rg.id = :releaseGroupId
        """

        private const val SELECT_RELEASES_IN_RELEASE_GROUP = """
            SELECT r.*
            $RELEASES_IN_RELEASE_GROUP
        """

        private const val SELECT_RELEASES_ID_IN_RELEASE_GROUP = """
            SELECT r.id
            $RELEASES_IN_RELEASE_GROUP
        """

        private const val ORDER_BY_RELEASE_GROUP_LINKING_TABLE = """
            ORDER BY rrg.rowid
        """

        private const val FILTERED = """
            AND (
                r.name LIKE :query OR r.disambiguation LIKE :query
                OR r.date LIKE :query OR r.country_code LIKE :query
                OR r.formats LIKE :query OR r.tracks LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM releases WHERE id IN (
        $SELECT_RELEASES_ID_IN_RELEASE_GROUP
        )
        """
    )
    abstract suspend fun deleteReleasesInReleaseGroup(releaseGroupId: String)

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
    @Query(
        """
        $SELECT_RELEASES_IN_RELEASE_GROUP
        $ORDER_BY_RELEASE_GROUP_LINKING_TABLE
    """
    )
    abstract fun getReleasesInReleaseGroup(releaseGroupId: String): PagingSource<Int, ReleaseRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_IN_RELEASE_GROUP
        $FILTERED
    """
    )
    abstract fun getReleasesInReleaseGroupFiltered(
        releaseGroupId: String,
        query: String
    ): PagingSource<Int, ReleaseRoomModel>
}
