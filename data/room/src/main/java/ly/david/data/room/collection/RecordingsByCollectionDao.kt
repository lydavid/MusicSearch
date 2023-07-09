package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.recording.RecordingForListItem

interface RecordingsByCollectionDao {

    companion object {
        private const val RECORDINGS_BY_COLLECTION = """
            FROM recording r
            INNER JOIN collection_entity ce ON r.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            LEFT JOIN artist_credit_entity acr ON acr.resource_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE c.id = :collectionId
        """

        private const val SELECT_RECORDINGS_BY_COLLECTION = """
            SELECT r.*, ac.name AS artist_credit_names
            $RECORDINGS_BY_COLLECTION
        """

        private const val SELECT_RECORDINGS_ID_BY_COLLECTION = """
            SELECT r.id
            $RECORDINGS_BY_COLLECTION
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
        $SELECT_RECORDINGS_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteRecordingsByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $RECORDINGS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfRecordingsByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RECORDINGS_BY_COLLECTION
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    fun getRecordingsByCollection(collectionId: String): PagingSource<Int, RecordingForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RECORDINGS_BY_COLLECTION
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    fun getRecordingsByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, RecordingForListItem>
}
