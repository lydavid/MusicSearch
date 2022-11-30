package ly.david.data.persistence.releasegroup

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries

// TODO: put this in release group dao? only used by ReleaseGroupRepository
//  artist should hold release groups by artist
@Dao
abstract class ReleasesReleaseGroupsDao {

    companion object {
        private const val RELEASES_IN_RELEASE_GROUP = """
            FROM releases r
            INNER JOIN release_groups rg ON rg.id = r.release_group_id
            LEFT JOIN artist_credits_resources acr ON acr.resource_id = r.id
            LEFT JOIN artist_credits ac ON ac.id = acr.artist_credit_id
            WHERE rg.id = :releaseGroupId
        """

        private const val SELECT_RELEASES_IN_RELEASE_GROUP = """
            SELECT r.*, ac.name AS artist_credit_names
            $RELEASES_IN_RELEASE_GROUP
        """

        private const val SELECT_RELEASES_ID_IN_RELEASE_GROUP = """
            SELECT r.id
            $RELEASES_IN_RELEASE_GROUP
        """

        private const val ORDER_BY_DATE_AND_NAME = """
            ORDER BY r.date, r.name
        """

        private const val FILTERED = """
            AND (
                r.name LIKE :query OR r.disambiguation LIKE :query
                OR r.date LIKE :query OR r.country_code LIKE :query
                OR r.formats LIKE :query OR r.tracks LIKE :query
                OR ac.name LIKE :query
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
    abstract suspend fun deleteReleasesByReleaseGroup(releaseGroupId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM releases r
            INNER JOIN release_groups rg ON rg.id = r.release_group_id
            WHERE rg.id = :releaseGroupId
            GROUP BY rg.id),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByReleaseGroup(releaseGroupId: String): Int

    // TODO: based on r's release_group_id
    @Transaction
    @Query(
        """
        $SELECT_RELEASES_IN_RELEASE_GROUP
        $ORDER_BY_DATE_AND_NAME
    """
    )
    abstract fun getReleasesByReleaseGroup(releaseGroupId: String): PagingSource<Int, ReleaseWithCreditsAndCountries>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_IN_RELEASE_GROUP
        $FILTERED
        $ORDER_BY_DATE_AND_NAME
    """
    )
    abstract fun getReleasesByReleaseGroupFiltered(
        releaseGroupId: String,
        query: String
    ): PagingSource<Int, ReleaseWithCreditsAndCountries>
}
