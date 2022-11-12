package ly.david.data.persistence.label

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries

@Dao
abstract class ReleasesLabelsDao : BaseDao<ReleaseLabel> {

    companion object {
        private const val RELEASES_BY_LABEL = """
            FROM releases r
            INNER JOIN releases_labels rl ON r.id = rl.release_id
            INNER JOIN labels l ON l.id = rl.label_id
            WHERE l.id = :labelId
        """

        // DISTINCT because junction table can repeat due to different catalog number for same release/label
        private const val SELECT_RELEASES_BY_LABEL = """
            SELECT DISTINCT r.*
            $RELEASES_BY_LABEL
        """

        private const val SELECT_RELEASES_ID_BY_LABEL = """
            SELECT r.id
            $RELEASES_BY_LABEL
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

    // Note: If a release was added via release group screen and label screen,
    // only one record would exist. So this method will remove that record from both
    // screens.
    // Since this method is to be used alongside refreshing,
    // we won't consider this a bug.
    @Query(
        """
        DELETE FROM releases WHERE id IN (
        $SELECT_RELEASES_ID_BY_LABEL
        )
        """
    )
    abstract suspend fun deleteReleasesByLabel(labelId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*) FROM
                (SELECT DISTINCT rl.release_id, rl.label_id
                FROM releases r
                INNER JOIN releases_labels rl ON r.id = rl.release_id
                INNER JOIN labels l ON l.id = rl.label_id
                WHERE l.id = :labelId)
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByLabel(labelId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_LABEL
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByLabel(labelId: String): PagingSource<Int, ReleaseWithReleaseCountries>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_LABEL
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByLabelFiltered(
        labelId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries>
}
