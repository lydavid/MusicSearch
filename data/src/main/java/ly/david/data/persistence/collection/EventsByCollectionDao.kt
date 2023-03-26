package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.event.EventRoomModel

interface EventsByCollectionDao {

    companion object {
        private const val EVENTS_BY_COLLECTION = """
            FROM event e
            INNER JOIN collection_entity ce ON e.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_EVENT_BY_COLLECTION = """
            SELECT e.*
            $EVENTS_BY_COLLECTION
        """

        private const val SELECT_EVENT_ID_BY_COLLECTION = """
            SELECT e.id
            $EVENTS_BY_COLLECTION
        """

        private const val ORDER_BY_DATE_NAME = """
            ORDER BY e.begin, e.end, e.name
        """

        private const val FILTERED = """
            AND (
                e.name LIKE :query OR e.disambiguation LIKE :query
                OR e.type LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM event WHERE id IN (
        $SELECT_EVENT_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteEventsByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $EVENTS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfEventsByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_EVENT_BY_COLLECTION
        $ORDER_BY_DATE_NAME
    """
    )
    fun getEventsByCollection(collectionId: String): PagingSource<Int, EventRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_EVENT_BY_COLLECTION
        $FILTERED
        $ORDER_BY_DATE_NAME
    """
    )
    fun getEventsByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, EventRoomModel>
}
