package ly.david.data.room.area.releases

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.release.ReleaseForListItem

@Dao
abstract class ReleaseCountryDao : BaseDao<ReleaseCountry>() {

    // TODO: one use for an intermediate country_area table
    //  is to make these more logical
    //  But we could just call country_id area_id
    companion object {
        private const val RELEASES_BY_COUNTRY = """
            FROM release r
            INNER JOIN release_country rc ON r.id = rc.release_id
            INNER JOIN area a ON a.id = rc.country_id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE a.id = :areaId
        """

        private const val SELECT_RELEASES_BY_COUNTRY = """
            SELECT r.*, ac.name AS artist_credit_names
            $RELEASES_BY_COUNTRY
        """

        private const val SELECT_RELEASES_ID_BY_COUNTRY = """
            SELECT r.id
            $RELEASES_BY_COUNTRY
        """

        private const val ORDER_BY_DATE_AND_TITLE = """
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

    // region by Area
    @Query(
        """
        DELETE FROM `release` WHERE id IN (
        $SELECT_RELEASES_ID_BY_COUNTRY
        )
        """
    )
    abstract suspend fun deleteReleasesByCountry(areaId: String)

    @Query("DELETE FROM release_country WHERE country_id = :areaId")
    abstract suspend fun deleteArtistReleaseLinks(areaId: String)

    /**
     * This is the actual number of releases from this country (area) stored locally.
     *
     * When doing browse pagination, we should not use this.
     */
    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM `release` r
            INNER JOIN release_country rc ON r.id = rc.release_id
            INNER JOIN area a ON a.id = rc.country_id
            WHERE a.id = :areaId
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByCountry(areaId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COUNTRY
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByCountry(areaId: String): PagingSource<Int, ReleaseForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COUNTRY
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByCountryFiltered(
        areaId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem>
    // endregion
}
