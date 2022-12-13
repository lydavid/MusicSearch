package ly.david.data.persistence.area

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries

@Dao
abstract class ReleasesCountriesDao : BaseDao<ReleaseCountry>() {

    // TODO: one use for an intermediate country_area table
    //  is to make these more logical
    //  But we could just call country_id area_id
    companion object {
        private const val RELEASES_BY_COUNTRY = """
            FROM releases r
            INNER JOIN releases_countries rc ON r.id = rc.release_id
            INNER JOIN areas a ON a.id = rc.country_id
            LEFT JOIN artist_credits_resources acr ON acr.resource_id = r.id
            LEFT JOIN artist_credits ac ON ac.id = acr.artist_credit_id
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
                OR r.formats LIKE :query OR r.tracks LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    // region by Area
    @Query(
        """
        DELETE FROM releases WHERE id IN (
        $SELECT_RELEASES_ID_BY_COUNTRY
        )
        """
    )
    abstract suspend fun deleteReleasesFromCountry(areaId: String)

    /**
     * This is the actual number of releases from this country (area) stored locally.
     *
     * When doing browse pagination, we should not use this.
     */
    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM releases r
            INNER JOIN releases_countries rc ON r.id = rc.release_id
            INNER JOIN areas a ON a.id = rc.country_id
            WHERE a.id = :areaId
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesFromCountry(areaId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COUNTRY
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByCountry(areaId: String): PagingSource<Int, ReleaseWithCreditsAndCountries>

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
    ): PagingSource<Int, ReleaseWithCreditsAndCountries>
    // endregion
}
