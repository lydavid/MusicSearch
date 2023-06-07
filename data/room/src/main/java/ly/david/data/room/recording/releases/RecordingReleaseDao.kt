package ly.david.data.room.recording.releases

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.release.ReleaseForListItem

@Dao
abstract class RecordingReleaseDao : BaseDao<RecordingRelease>() {

    companion object {
        private const val RELEASES_BY_RECORDING = """
            FROM release rel
            INNER JOIN recording_release rr ON rel.id = rr.release_id
            INNER JOIN recording rec ON rec.id = rr.recording_id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = rel.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE rec.id = :recordingId
        """

        private const val SELECT_RELEASES_BY_RECORDING = """
            SELECT rel.*, ac.name AS artist_credit_names
            $RELEASES_BY_RECORDING
        """

        private const val SELECT_RELEASES_ID_BY_RECORDING = """
            SELECT rel.id
            $RELEASES_BY_RECORDING
        """

        private const val ORDER_BY_DATE_AND_TITLE = """
            ORDER BY rel.date, rel.name
        """

        private const val FILTERED = """
            AND (
                rel.name LIKE :query OR rel.disambiguation LIKE :query
                OR rel.date LIKE :query OR rel.country_code LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    // Note: If a release was added via release group screen and recording screen,
    // only one record would exist. So this method will remove that record from both
    // screens.
    // Since this method is to be used alongside refreshing,
    // we won't consider this a bug.
    @Query(
        """
        DELETE FROM release WHERE id IN (
        $SELECT_RELEASES_ID_BY_RECORDING
        )
        """
    )
    abstract suspend fun deleteReleasesByRecording(recordingId: String)

    @Query("DELETE FROM recording_release WHERE recording_id = :recordingId")
    abstract suspend fun deleteRecordingReleaseLinks(recordingId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $RELEASES_BY_RECORDING
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByRecording(recordingId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_RECORDING
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByRecording(recordingId: String): PagingSource<Int, ReleaseForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_RECORDING
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByRecordingFiltered(
        recordingId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem>
}
