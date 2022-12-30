package ly.david.data.persistence.release.releasegroup

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseForListItem

// TODO: put this in release group dao? only used by ReleaseGroupRepository
//  artist should hold release groups by artist
@Dao
abstract class ReleaseReleaseGroupDao : BaseDao<ReleaseReleaseGroup>() {

    companion object {
        private const val RELEASES_BY_RELEASE_GROUP = """
            FROM release r
            INNER JOIN release_release_group rrg ON rrg.release_id = r.id
            INNER JOIN release_group rg ON rg.id = rrg.release_group_id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE rg.id = :releaseGroupId
        """

        private const val SELECT_RELEASES_BY_RELEASE_GROUP = """
            SELECT r.*, ac.name AS artist_credit_names
            $RELEASES_BY_RELEASE_GROUP
        """

        private const val SELECT_RELEASES_ID_BY_RELEASE_GROUP = """
            SELECT r.id
            $RELEASES_BY_RELEASE_GROUP
        """

        private const val ORDER_BY_DATE_AND_NAME = """
            ORDER BY r.date, r.name
        """

        private const val FILTERED = """
            AND (
                r.name LIKE :query OR r.disambiguation LIKE :query
                OR r.date LIKE :query OR r.country_code LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM release WHERE id IN (
        $SELECT_RELEASES_ID_BY_RELEASE_GROUP
        )
        """
    )
    abstract suspend fun deleteReleasesByReleaseGroup(releaseGroupId: String)

    @Query("DELETE FROM release_release_group WHERE release_group_id = :releaseGroupId")
    abstract suspend fun deleteReleaseReleaseGroupLinks(releaseGroupId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM release r
            INNER JOIN release_release_group rrg ON rrg.release_id = r.id
            INNER JOIN release_group rg ON rg.id = rrg.release_group_id
            WHERE rg.id = :releaseGroupId
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByReleaseGroup(releaseGroupId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_RELEASE_GROUP
        $ORDER_BY_DATE_AND_NAME
    """
    )
    abstract fun getReleasesByReleaseGroup(releaseGroupId: String): PagingSource<Int, ReleaseForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_RELEASE_GROUP
        $FILTERED
        $ORDER_BY_DATE_AND_NAME
    """
    )
    abstract fun getReleasesByReleaseGroupFiltered(
        releaseGroupId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem>
}
