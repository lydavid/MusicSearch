package ly.david.data.persistence.recording

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries

@Dao
abstract class ReleasesRecordingsDao : BaseDao<ReleaseRecording> {

    companion object {
        private const val RELEASES_BY_RECORDING = """
            FROM releases rel
            INNER JOIN releases_recordings rr ON rel.id = rr.release_id
            INNER JOIN recordings rec ON rec.id = rr.recording_id
            WHERE rec.id = :recordingId
        """

        private const val SELECT_RELEASES_BY_RECORDING = """
            SELECT rel.*
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
                OR rel.formats LIKE :query OR rel.tracks LIKE :query
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
        DELETE FROM releases WHERE id IN (
        $SELECT_RELEASES_ID_BY_RECORDING
        )
        """
    )
    abstract suspend fun deleteReleasesByRecording(recordingId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM releases rel
            INNER JOIN releases_recordings rr ON rel.id = rr.release_id
            INNER JOIN recordings rec ON rec.id = rr.recording_id
            WHERE rec.id = :recordingId
            GROUP BY rec.id),
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
    abstract fun getReleasesByRecording(recordingId: String): PagingSource<Int, ReleaseWithReleaseCountries>

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
    ): PagingSource<Int, ReleaseWithReleaseCountries>
}
