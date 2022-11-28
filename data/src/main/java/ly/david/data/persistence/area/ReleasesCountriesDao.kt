package ly.david.data.persistence.area

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries

@Dao
abstract class ReleasesCountriesDao : BaseDao<ReleaseCountry> {

    // TODO: one use for an intermediate country_area table
    //  is to make these more logical
    //  But we could just call country_id area_id
    companion object {
        private const val RELEASES_BY_COUNTRY = """
            FROM releases r
            INNER JOIN releases_countries rc ON r.id = rc.release_id
            INNER JOIN areas a ON a.id = rc.country_id
            WHERE a.id = :areaId
        """

        private const val SELECT_RELEASES_BY_COUNTRY = """
            SELECT r.*
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
            GROUP BY a.id),
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
    abstract fun getReleasesFromCountry(areaId: String): PagingSource<Int, ReleaseWithReleaseCountries>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COUNTRY
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesFromCountryFiltered(
        areaId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries>
    // endregion

    // region by Release

    @Transaction
    @Query(
        """
        SELECT a.*, rc.date
        FROM areas a
        INNER JOIN releases_countries rc ON rc.country_id = a.id
        INNER JOIN releases r ON r.id = rc.release_id
        WHERE r.id = :releaseId
        ORDER BY a.name
    """
    )
    abstract suspend fun getAreasWithReleaseDate(releaseId: String): List<AreaWithReleaseDateOld>
    // endregion
}
