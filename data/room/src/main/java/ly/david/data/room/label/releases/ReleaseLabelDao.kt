package ly.david.data.room.label.releases

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.release.ReleaseForListItem

@Dao
abstract class ReleaseLabelDao : BaseDao<ReleaseLabel>() {

    companion object {
        private const val RELEASES_BY_LABEL = """
            FROM release r
            INNER JOIN release_label rl ON r.id = rl.release_id
            INNER JOIN label l ON l.id = rl.label_id
            LEFT JOIN artist_credit_entity acr ON acr.entity_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE l.id = :labelId
        """

        // DISTINCT because junction table can repeat due to different catalog number for same release/label
        private const val SELECT_RELEASES_BY_LABEL = """
            SELECT DISTINCT r.*, ac.name AS artist_credit_names
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
                OR ac.name LIKE :query
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
        DELETE FROM `release` WHERE id IN (
        $SELECT_RELEASES_ID_BY_LABEL
        )
        """
    )
    abstract suspend fun deleteReleasesByLabel(labelId: String)

    @Query("DELETE FROM release_label WHERE label_id = :labelId")
    abstract suspend fun deleteLabelReleaseLinks(labelId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*) FROM
                (SELECT DISTINCT rl.release_id, rl.label_id
                FROM `release` r
                INNER JOIN release_label rl ON r.id = rl.release_id
                INNER JOIN label l ON l.id = rl.label_id
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
    abstract fun getReleasesByLabel(labelId: String): PagingSource<Int, ReleaseForListItem>

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
    ): PagingSource<Int, ReleaseForListItem>
}
