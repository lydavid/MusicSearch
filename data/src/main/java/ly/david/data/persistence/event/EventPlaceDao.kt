package ly.david.data.persistence.event

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class EventPlaceDao : BaseDao<EventPlace>() {

    companion object {
        private const val EVENTS_BY_PLACE = """
            FROM event e
            INNER JOIN event_place ep ON e.id = ep.event_id
            INNER JOIN place p ON p.id = ep.place_id
            WHERE p.id = :placeId
        """

        private const val SELECT_EVENT_BY_PLACE = """
            SELECT e.*
            $EVENTS_BY_PLACE
        """

        private const val SELECT_EVENT_ID_BY_PLACE = """
            SELECT e.id
            $EVENTS_BY_PLACE
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
        $SELECT_EVENT_ID_BY_PLACE
        )
        """
    )
    abstract suspend fun deleteEventsByPlace(placeId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $EVENTS_BY_PLACE
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfEventsByPlace(placeId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_EVENT_BY_PLACE
        $ORDER_BY_DATE_NAME
    """
    )
    abstract fun getEventsByPlace(placeId: String): PagingSource<Int, EventRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_EVENT_BY_PLACE
        $FILTERED
        $ORDER_BY_DATE_NAME
    """
    )
    abstract fun getEventsByPlace(
        placeId: String,
        query: String
    ): PagingSource<Int, EventRoomModel>
}
