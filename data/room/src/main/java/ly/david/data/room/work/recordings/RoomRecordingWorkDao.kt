package ly.david.data.room.work.recordings

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.recording.RecordingForListItem

@Dao
abstract class RoomRecordingWorkDao : BaseDao<RecordingWork>() {

    companion object {
        private const val RECORDINGS_BY_WORK = """
            FROM recording r
            INNER JOIN recording_work rw ON r.id = rw.recording_id
            INNER JOIN work w ON w.id = rw.work_id
            LEFT JOIN artist_credit_entity acr ON acr.entity_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE w.id = :workId
        """

        private const val SELECT_RECORDINGS_BY_WORK = """
            SELECT r.*, ac.name AS artist_credit_names
            $RECORDINGS_BY_WORK
        """

        private const val SELECT_RECORDINGS_ID_BY_WORK = """
            SELECT r.id
            $RECORDINGS_BY_WORK
        """

        private const val ORDER_BY_DATE_AND_TITLE = """
            ORDER BY r.first_release_date, r.title
        """

        private const val FILTERED = """
            AND (
                r.title LIKE :query OR r.disambiguation LIKE :query
                OR r.first_release_date LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM recording WHERE id IN (
        $SELECT_RECORDINGS_ID_BY_WORK
        )
        """
    )
    abstract suspend fun deleteRecordingsByWork(workId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $RECORDINGS_BY_WORK
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfRecordingsByWork(workId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RECORDINGS_BY_WORK
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getRecordingsByWork(workId: String): PagingSource<Int, RecordingForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RECORDINGS_BY_WORK
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getRecordingsByWorkFiltered(
        workId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem>
}
